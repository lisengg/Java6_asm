const app = angular.module("shopping-cart-app",[]);

app.controller("shopping-cart-ctrl",function($scope,$http){
	/*
	QUẢN LÝ GIỎ HÀNG
	*/
	$scope.cart = {
		items:[],
		//Thêm sản phẩm vào giỏ hàng
		add(id){
			var item = this.items.find(item => item.id == id);
			if(item){//dựa vào id để kiểm tra, this.items đi kiểm tra xem có item chưa nếu có sẽ tăng số lượng lên 
				item.qty++;
				this.saveToLocalStorage();	
			}
			else{
			  $http.get(`/rest/products/${id}`).then(resp => {//Nếu chưa có tải ap trên server về thông qua API
				resp.data.qty = 1;//sau khi tải về xong đặt số lượng bằng 1
				this.items.push(resp.data);// và bỏ vào danh sách mặt hàng đã chọn
				this.saveToLocalStorage();// danh sách mặt hàng là 1 mảng lưu vào local
			  })
			}
		},
		//Xóa sản phẩm khỏi giỏ hàng
		remove(id){
			var index = this.items.findIndex(item => item.id == id);//tìm ra vị trí của ap nằm trong giở hàng thông qua id sp
			this.items.splice(index, 1);// khi tìm dc dùng phương thức splice để xóa 1 phần tử khỏi mảng
			this.saveToLocalStorage();// xóa xong lưu lại
		},
		//Xóa sạch các mặt hàng trong giỏ
		clear(){
			this.items = []//cho vào mảng rỗng sau đó lưu vào LocalStorage
			this.saveToLocalStorage();
		},
		//Tính thành tiền 1 sản phẩm
		amt_of(item){},
		//Tính tổng số lượng các mặt hàng trong giỏ
		get count(){
			return this.items
			.map(item => item.qty)
			.reduce((total, qty) => total += qty,0);
		},
		//Tổng thành tiền các mặt hàng trong giở
		get amount(){
			return this.items
			.map(item => item.qty * item.price) // tổng số lượng nhân đơn giá ra tổng tiền
			.reduce((total, qty) => total += qty,0);
		},
		//Lưu giỏ hàng vào local storage
		saveToLocalStorage(){
			var json = JSON.stringify(angular.copy(this.items));//dùng anlular copy và đổi mặt hàng sang json
			localStorage.setItem("cart",json);//sau đó dùng chuỗi json đó lưu vào trong localStorage với cái tên là "cart"
		},
		//Đọc giỏ hàng từ local storage
		loadFromLocalStorage(){
			var json = localStorage.getItem("cart");// đọc lại cart từ local storage ra
			this.items = json ? JSON.parse(json):[];//nếu có chuyển sang json và gán vô items nếu k gán vô mảng rỗng
		}		
	}
	$scope.cart.loadFromLocalStorage();	
	
	$scope.order = {
		createDate: new Date(),
		address:"",
		account: {username: $("#username").text()},
		get orderDetails(){
			return $scope.cart.items.map(item => {
				return {// duyệt qua mỗi mặt hàng trong giỏ hàng
					product:{id: item.id},
					price: item.price,
					quantity: item.qty
				}
			});
		},
		purchase(){
			var order = angular.copy(this);
			//Thực hiện đặt hàng
			$http.post("/rest/orders", order).then(resp => {// lấy địa hcir gửi lên sever
				alert("Đặt hàng thành công!")//sau khi đặt hàng thành công sẽ xóa giỏ hàng
				$scope.cart.clear();
				location.href = "/order/detail/" + resp.data.id;//đồng thời chuyển đến tảng chi tiết đơn hàng
			}).catch(error => {
				alert("Đặt hàng lỗi!")
				console.log(error)
			})
		}
	}
})