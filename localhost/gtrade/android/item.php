<?php
$item_id = $_POST['item_id'];

$con=mysqli_connect("localhost","root","","online_trading");

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$query="SELECT * FROM items where item_id='$item_id'";
//echo $query;
$result=mysqli_query($con,$query);
if(mysqli_num_rows($result)==0){
	echo "Item not found.";
}
else{
	$row = mysqli_fetch_array($result);
	//echo $item_id;
	$thumbnail = file_get_contents("http://localhost/gtrade/upload/".$row['pic_loc']);
	$encoded = base64_encode($thumbnail);
	header('Content-Type: application/json');
	$row['image']=$encoded;
	echo json_encode($row);
	//print_r($row);
}
mysqli_close($con);
?>
