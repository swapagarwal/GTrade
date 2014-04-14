<?php
$username = $_POST['username'];
$password = $_POST['password'];

$con=mysqli_connect("localhost","root","","online_trading");

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$query="SELECT * FROM user where user_nm='$username' and pass='$password'";
//echo $query;
$result=mysqli_query($con,$query);
if(mysqli_num_rows($result)==0){
	echo "Incorrect combination";
}
else{
	$row = mysqli_fetch_array($result);
	echo "Welcome ".$row{'user_nm'};
	//print_r($row);
}
mysqli_close($con);
?>
