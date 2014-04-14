<?php
$item_id = $_GET['item_id'];
if(isset($_GET['user_nm'])){
	$user_nm = $_GET['user_nm'];
	if($user_nm==""){
		echo "Please login first.";
	}
	else{
		$con=mysqli_connect("localhost","root","","online_trading");

		if (mysqli_connect_errno())
		  {
		  echo "Failed to connect to MySQL: " . mysqli_connect_error();
		  }

		$query="INSERT INTO `cart`(`user_nm`,`qty`,`item_id`) VALUES ('$user_nm',1,$item_id)";
		//echo $query;
		$result=mysqli_query($con,$query);
		if($result){
			echo "Added to cart.";
		}
		else{
			echo "Couldn't add to cart.";
		}
		mysqli_close($con);
	}
}
else{
	echo "Please login first.";
}
?>
