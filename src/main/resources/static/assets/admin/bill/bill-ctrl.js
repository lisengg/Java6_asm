app.controller("bill-ctrl",function($scope,$http){
	$scope.orders = [];
	$scope.orderdetails = [];
	$scope.items = [];
	$scope.form = {};
	
	$scope.initialize = function(){
		//load orders
		$http.get("/rest/orders").then(resp =>{// dùng get để lấy order về
			$scope.orders = resp.data; //bỏ vào orders trên để nó hiện ra table
			$scope.orders.forEach(order => {// trước khi bỏ vào chuyển đổi kiểu ngày tháng trong sql thành chuỗi
				order.createDate = new Date(order.createDate)
			})
		});
		//load orderdetails
		$http.get("/rest/orderdetails").then(resp => {
			$scope.orderdetails = resp.data;
		})
		//load products
		$http.get("/rest/products").then(resp =>{// dùng get để lấy sp về
			$scope.items = resp.data; //bỏ vào items trên để nó hiện ra table
		});
	}
	//Khởi đầu
	$scope.initialize();
	
	//Xóa form
	$scope.reset = function(){
		$scope.form = {
			createDate: new Date(),
			image: 'cloud-upload.jpg',
		};
	}
	//Hiển thị lên form
	$scope.edit = function(orderdetail){
		$scope.form = angular.copy(orderdetail);
		$(".nav-tabs a:eq(0)").tab('show')
	}
	//Cập nhật hóa đơn chi tiết
	$scope.update = function(){
		var orderdetail = angular.copy($scope.form);
		$http.put(`/rest/orderdetails/${orderdetail.id}`, orderdetail).then(resp => {
			var index = $scope.orderdetails.findIndex(p => p.id == orderdetail.id);
			$scope.orderdetails[index] = orderdetail;
			alert("Cập nhật hóa đơn chi tiết thành công!")
		})
		.catch(error => {
			alert("Lỗi cập nhật hóa đơn chi tiết!");
			console.log("Error", error);
		});
	}
	//Xóa hóa đơn chi tiết
	$scope.delete = function(orderdetail){
		$http.delete(`/rest/orderdetails/${orderdetail.id}`).then(resp => {
			var index = $scope.orderdetails.findIndex(p => p.id == orderdetail.id);
			$scope.orderdetails.splice(index, 1);
			$scope.reset();
			alert("Xóa hóa đơn chi tiết thành công!")
		})
		.catch(error => {
			alert("Lỗi xóa hóa đơn chi tiết!");
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
