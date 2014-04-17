<?php
$user_nm = $_POST['user_nm'];

$con=mysqli_connect("localhost","root","","online_trading");

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$query="SELECT * FROM watch_list where user_nm = '$user_nm'";
//echo $query;
$result=mysqli_query($con,$query);
if(mysqli_num_rows($result)==0){
	echo "Your watch list is empty.";
}
else{
	//echo mysqli_num_rows($result)." results found...";
	$results=array();
	while($row = mysqli_fetch_array($result)){
		$i=$row['item_id'];
		$q="SELECT * FROM items where item_id='$i'";
		$r=mysqli_fetch_array(mysqli_query($con,$q));
		$results[]=array('item_nm'=>$r['item_nm'],'item_id'=>$row['item_id'],'type'=>$r['type']);
	}
	echo json_encode($results);
}
mysqli_close($con);
?>
