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
	//echo mysqli_num_rows($result)." results found...";
	$results=array();
	while($row = mysqli_fetch_array($result)){
		$results[]=array('item_nm'=>$row['item_nm'],'item_id'=>$row['item_id'],'type'=>$row['type']);
	}
	echo json_encode($results);
}
mysqli_close($con);
?>
