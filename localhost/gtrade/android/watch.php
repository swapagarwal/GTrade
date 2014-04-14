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
		
		$query="SELECT * FROM `watch_list` WHERE user_nm ='$user_nm' and item_id='$item_id'";
		$result=mysqli_query($con,$query);
		if(mysqli_num_rows($result)){
			echo "Already in watch list.";
		}
		else{
			$query="INSERT INTO `watch_list`(`user_nm`,`item_id`) VALUES ('$user_nm','$item_id')";
			//echo $query;
			$result=mysqli_query($con,$query);
			if($result){
				echo "Added to watch list.";
			}
			else{
				echo "Couldn't add to watch list.";
			}
		}
		mysqli_close($con);
	}	
}
else{
	echo "Please login first.";
}
?>
