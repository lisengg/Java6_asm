app.controller("authority-ctrl",function($scope,$http,$location){
	$scope.roles = [];
	$scope.admins = [];
	$scope.authorities = [];
	$scope.form = {};
	
		$scope.initialize = function(){
		//load all role
		$http.get("/rest/roles").then(resp => {
			$scope.roles = resp.data;
		})
		
		//load staffs and directors (administrators)
		$http.get("/rest/accounts").then(resp => {
			$scope.admins = resp.data;
		})
		
		//load authorites of staffs and directors
		$http.get("/rest/authorities").then(resp => {
			$scope.authorities = resp.data;
		}).catch(error => {
			$location.path("/unauthorized");
		})
	}

	//Khởi đầu
	$scope.initialize();
	
	$scope.authority_of = function(acc, role){
		if($scope.authorities){
			return $scope.authorities.find(ur => ur.account.username == acc.username && ur.role.id==role.id);
		}
	}
	
	$scope.authority_changed = function(acc, role){
		var authority = $scope.authority_of(acc, role);
		if(authority){//đã cấp quyền => thu hồi quyền(xóa)
			$scope.revoke_authority(authority);
		}
		else{// chưa được cấp quyền => cấp quyền (thêm mới)
			authority = {account: acc, role: role};
			$scope.grant_authority(authority);
		}
	}
	
	//Thêm mới authority
	$scope.grant_authority = function(authority){
		$http.post(`/rest/authorities`, authority).then(resp => {
			$scope.authorities.push(resp.data)
			alert("Cấp quyền sử dụng thành công")
		}).catch(error => {
			alert("Cấp quyền sử dụng thất bại")
			console.log("Error",error);
		})
	}
	//Xóa authority
	$scope.revoke_authority = function(authority){
		$http.delete(`/rest/authorities/${authority.id}`).then(resp => {
			var index = $scope.authorities.findIndex(a => a.id == authority.id);
			
			$scope.authorities.splice(index, 1);
			alert("Thu hồi quyền sử dụng thành công");
		}).catch(error => {
			alert("Thu hồi quyền sử dụng thất bại");
			console.log("Error",error);
		})
	}
		//Xóa form
	$scope.reset = function(){
		$scope.form = {
			image: 'user.jpg',
			available:true,
		};
	}
	//Hiển thị lên form
	$scope.edit = function(acc){
		$scope.form = angular.copy(acc);
		$(".nav-tabs a:eq(0)").tab('show')
	}
	//Thêm sản phẩm mới
	$scope.create = function(){
		var acc = angular.copy($scope.form);
		$http.post(`/rest/accounts`, acc).then(resp => {
			resp.data.createDate = new Date(resp.data.createDate)
			$scope.admins.push(resp.data);
			$scope.reset();
			alert("Thêm mới tài khoản thành công!")
		}).catch(error => {
			alert("Lỗi thêm mới tài khoản!");
			console.log("Error", error);
		});
	}

});