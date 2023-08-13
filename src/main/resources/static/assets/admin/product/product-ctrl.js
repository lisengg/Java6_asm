app.controller("product-ctrl",function($scope,$http){
	$scope.items = [];
	$scope.cates = [];
	$scope.form = {};
	
	$scope.initialize = function(){
		//load products
		$http.get("/rest/products").then(resp =>{// dùng get để lấy sp về
			$scope.items = resp.data; //bỏ vào items trên để nó hiện ra table
			$scope.items.forEach(item => {// trước khi bỏ vào chuyển đổi kiểu ngày tháng trong sql thành chuỗi
				item.createDate = new Date(item.createDate)
			})
		});
		//load categories
		$http.get("/rest/categories").then(resp =>{
			$scope.cates = resp.data; 
		});
	}
	//Khởi đầu
	$scope.initialize();
	
	//Xóa form
	$scope.reset = function(){
		$scope.form = {
			createDate: new Date(),
			image: 'cloud-upload.jpg',
			available:true,
		};
	}
	//Hiển thị lên form
	$scope.edit = function(item){
		$scope.form = angular.copy(item);
		$(".nav-tabs a:eq(0)").tab('show')
	}
	//Thêm sản phẩm mới
	$scope.create = function(){
		var item = angular.copy($scope.form);
		$http.post(`/rest/products`, item).then(resp => {
			resp.data.createDate = new Date(resp.data.createDate)
			$scope.items.push(resp.data);
			$scope.reset();
			alert("Thêm mới sản phẩm thành công!")
		}).catch(error => {
			alert("Lỗi thêm mới sản phẩm!");
			console.log("Error", error);
		});
	}
	//Cập nhật sản phẩm
	$scope.update = function(){
		var item = angular.copy($scope.form);
		$http.put(`/rest/products/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			alert("Cập nhật sản phẩm thành công!")
		})
		.catch(error => {
			alert("Lỗi cập nhật sản phẩm!");
			console.log("Error", error);
		});
	}
	//Xóa sản phẩm
	$scope.delete = function(item){
		$http.delete(`/rest/products/${item.id}`).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items.splice(index, 1);
			$scope.reset();
			alert("Xóa sản phẩm thành công!")
		})
		.catch(error => {
			alert("Lỗi xóa sản phẩm!");
			console.log("Error", error);
		});
	}
	//Upload Hình
	$scope.imageChanged = function(files){
		var data = new FormData();// lấy đối tượng formdata
		data.append('file',files[0]);//lấy file đã chọn bỏ vào formdata 
		$http.post('/rest/upload/images', data,{//post lên trên sever
			transformRequest: angular.identity,
			headers: {'Content-Type': undefined}
		}).then(resp => {
			$scope.form.image = resp.data.name;//trả về resp, và lấy name trong data và thế vào thuộc tính form.image để nó hiện ảnh lên giao diện
		}).catch(error => {
			alert("Lỗi upload hình ảnh");
			console.log("Error", error);
		})
	}
	
	$scope.pager = {
	    page: 0,
	    size: 10,
	    get items(){
	        var start = this.page * this.size;
	        return $scope.items.slice(start, start + this.size);
	    },
	    get count(){
	        return Math.ceil(1.0 * $scope.items.length / this.size);
	    },
	    first(){
	        this.page = 0;
	    },
	    prev(){
	        this.page--;
	        if (this.page < 0) {
	            this.last();
	        }
	    },
	    next(){
	        this.page++;
	        if (this.page >= this.count) {
	            this.first();
	        }
	    },
	    last(){
	        this.page = this.count - 1;
	    }
	}
});
