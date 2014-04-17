<?php
$item_id = $_GET['item_id'];
$bid = $_GET['bid'];
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
		$query="INSERT INTO `auction_bidder`(`user_nm`,`bid`,`item_id`) VALUES ('$user_nm',$bid,$item_id)";
		//echo $query;
		$result=mysqli_query($con,$query);
		if($result){
			echo "Bid placed successfully.";
		}
		else{
			echo "Couldn't place bid.";
		}
		mysqli_close($con);
	}
}
else{
	echo "Please login first.";
}
?>
