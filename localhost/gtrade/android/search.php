<?php
$search = $_POST['search'];

$con=mysqli_connect("localhost","root","","online_trading");

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$query="SELECT * FROM items where item_nm LIKE '%$search%'";
//echo $query;
$result=mysqli_query($con,$query);
if(mysqli_num_rows($result)==0){
	echo "No results found.";
}
else{
	$row = mysqli_fetch_array($result);
	echo mysqli_num_rows($result)." results found...";
	//print_r($row);
}
mysqli_close($con);
?>
