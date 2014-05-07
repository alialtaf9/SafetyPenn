<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 *
 * Each request will be identified by TAG
 * Response will be JSON data
 
  /**
 * check for POST request
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0, "error_msg"=>"");
    $conn =mysqli_connect('localhost', 'root', 'SafetyPenn', 'login');
    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];
        // check for user
	$result = mysqli_query($conn,"SELECT * FROM members WHERE email = '$email'");
	$no_of_rows = mysqli_num_rows($result);
	if ($no_of_rows > 0) {
	    $result = mysqli_fetch_array($result);
	    $encrypted_password = $result['encrypted_password'];
	    // check for password equality
	    if ($encrypted_password == $password) {
	        // user authentication details are correct
	        $response["success"] = 1;
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                echo json_encode($response);
	    }
	} else {
	    // user not found
	    $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
	    echo json_encode($response);
	}
    }
    else if ($tag == 'register') {
        // Request type is Register new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
        $eye_color = $_POST['eye_color'];
        $hair_color = $_POST['hair_color'];
        $height = $_POST['height'];
        $gender = $_POST['gender'];
        $weight = $_POST['weight'];
	$user_number = $_POST['user_number'];
	$emergency_number = $_POST['emergency_number'];
	// check if user already exists
 	$result = mysqli_query($conn,"SELECT email from members WHERE email = '$email'");
	$no_of_rows = mysqli_num_rows($result);
	if ($no_of_rows > 0) {
	    // user already exists - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already exists";
            echo json_encode($response);
	} else {
	    $user = mysqli_query($conn,"INSERT INTO members (email, name, encrypted_password) VALUES ('$email', '$name', '$password')");
	    if ($user == false) {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = mysqli_error($conn);
                echo json_encode($response);

            } else { 
	      $image = $_FILES['picture'];
	      if($_FILES['picture']['error'] > 0) {
		$response["error"] = 1;
                $response["error_msg"] = $_FILES['picture']['error'];
                echo json_encode($response);

	      }
	      $pic_name = $_FILES['picture']['name'];
	      move_uploaded_file($_FILES['picture']['tmp_name'], getcwd().'/images/'.$pic_name); 
		    
	      $url='http://6ac30af9.ngrok.com/addmember';
		$fields = array(
		'name' => $name,
	    	'email' => $email,
		'eye_color' => $eye_color,
		'hair_color' => $hair_color,
		'height' => $height,
		'gender' => $gender, 
		'weight' => $weight,
		'user_number' => $user_number, 
		'emergency_number' => $emergency_number,
		'picture' => '54.85.24.39/images/'.$pic_name
		);
		foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
		rtrim($fields_string, '&');

		//open connection
		$ch = curl_init();
	
		//set the url, number of POST vars, POST data
		curl_setopt($ch,CURLOPT_URL, $url);
		curl_setopt($ch,CURLOPT_POST, count($fields));
		curl_setopt($ch,CURLOPT_POSTFIELDS, $fields_string);

		//execute post
		$result = curl_exec($ch);
		//close connection
		curl_close($ch);
		if($result) {
                // user stored successfully
		    $response["success"] = 1;
		    $response["user"]["name"] = $name;
		    $response["user"]["email"] = $email;
		    echo json_encode($response);
		}
		else {
		  $response["error"] = 1;
		  $response["error_msg"] = "Post error";
             	  echo json_encode($response);	
		  }
	     	      
	    }
	}
    }
    else if($tag == "timerCritical") {
        $email = $_POST['email'];
	$lat = $_POST['lat'];
	$lon = $_POST['lon'];
	//	$lat = '39.95297';
	//	$lon = '-75.20139';
	$url='http://6ac30af9.ngrok.com/addnotification';
	$fields = array(
	'type' => 'timer',
    	'email' => $email,
    	'latitude' => $lat,
	'longitude' => $lon	
	);
	foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
	rtrim($fields_string, '&');

	//open connection
	$ch = curl_init();
	
	//set the url, number of POST vars, POST data
	curl_setopt($ch,CURLOPT_URL, $url);
	curl_setopt($ch,CURLOPT_POST, count($fields));
	curl_setopt($ch,CURLOPT_POSTFIELDS, $fields_string);

	//execute post
	$result = curl_exec($ch);
	//close connection
	curl_close($ch);
	//Post request sent successfully
	if($result) {
	     	$response["success"] = 1;
             	echo json_encode($response);
	}
	else {
		$response["error"] = 1;
		$response["error_msg"] = "Post error";
             	echo json_encode($response);	
	}


	}
    else if($tag == "escort") {
        $email = $_POST['email'];
	$lat = $_POST['lat'];
	$lon = $_POST['lon'];
	//	$lat = '39.95297';
	//	$lon = '-75.20139';
	$url='http://6ac30af9.ngrok.com/addnotification';
	$fields = array(
	'type' => $tag,
    	'email' => $email,
    	'latitude' => $lat,
	'longitude' => $lon	
	);
	foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
	rtrim($fields_string, '&');

	//open connection
	$ch = curl_init();
	
	//set the url, number of POST vars, POST data
	curl_setopt($ch,CURLOPT_URL, $url);
	curl_setopt($ch,CURLOPT_POST, count($fields));
	curl_setopt($ch,CURLOPT_POSTFIELDS, $fields_string);

	//execute post
	$result = curl_exec($ch);
	//close connection
	curl_close($ch);
	//Post request sent successfully
	if($result) {
	     	$response["success"] = 1;
             	echo json_encode($response);
	}
	else {
		$response["error"] = 1;
		$response["error_msg"] = "Post error";
             	echo json_encode($response);	
	}


    }
    else if($tag == "updatePic") {
      $email = $_POST['email'];
      $image = $_FILES['picture'];
      if($_FILES['picture']['error'] > 0) {
	$response["error"] = 1;
        $response["error_msg"] = $_FILES['picture']['error'];
        echo json_encode($response);

      }
      $pic_name = $_FILES['picture']['name'];
      move_uploaded_file($_FILES['picture']['tmp_name'], getcwd().'/images/'.$pic_name); 
	$url='http://safetypenn.herokuapp.com/edit';
	$fields = array(
	'email' => $email,
    	'picture' => '54.85.24.39/images/'.$pic_name	
	);
	foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
	rtrim($fields_string, '&');

	//open connection
	$ch = curl_init();
	
	//set the url, number of POST vars, POST data
	curl_setopt($ch,CURLOPT_URL, $url);
	curl_setopt($ch,CURLOPT_POST, count($fields));
	curl_setopt($ch,CURLOPT_POSTFIELDS, $fields_string);

	//execute post
	$result = curl_exec($ch);
	//close connection
	curl_close($ch);
	//Post request sent successfully
	if($result) {
	     	$response["success"] = 1;
             	echo json_encode($response);
	}
	else {
		$response["error"] = 1;
		$response["error_msg"] = "Post error";
             	echo json_encode($response);	
	}

    }

    else if($tag == "updateEmergencyContact") {
	$email = $_POST['email'];
	$number = $_POST['number'];
	$url='http://http://6ac30af9.ngrok.com/edit';
	$fields = array(
	'email' => $email,
    	'emergency_number' => $number	
	);
	foreach($fields as $key=>$value) { $fields_string .= $key.'='.$value.'&'; }
	rtrim($fields_string, '&');

	//open connection
	$ch = curl_init();
	
	//set the url, number of POST vars, POST data
	curl_setopt($ch,CURLOPT_URL, $url);
	curl_setopt($ch,CURLOPT_POST, count($fields));
	curl_setopt($ch,CURLOPT_POSTFIELDS, $fields_string);

	//execute post
	$result = curl_exec($ch);
	//close connection
	curl_close($ch);
	//Post request sent successfully
	if($result) {
	     	$response["success"] = 1;
             	echo json_encode($response);
	}
	else {
		$response["error"] = 1;
		$response["error_msg"] = "Post error";
             	echo json_encode($response);	
	}

    }
    else if ($tag == 'updatePassword') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];
        // check for user
	$result = mysqli_query($conn,"UPDATE members set encrypted_password = '$password' where email='$email'");
	$no_of_aff_rows = mysqli_affected_rows($result);
	if ($no_of_aff_rows > 0) {
	    $result = mysqli_fetch_array($result);
	    $encrypted_password = $result['encrypted_password'];
	    $response["success"] = 1;
	    echo json_encode($response);
	    }
	else {
	    // user not found
	    $response["error"] = 1;
            $response["error_msg"] = "Error resetting password";
	    echo json_encode($response);
	}
    }

    else {
            $response = array("error" => 1, "error_msg"=>"No valid tag");
	    echo json_encode($response);
    }
}
else {
            $response = array("error" => 1, "error_msg"=>"No tag");
	    echo json_encode($response);
}
?>
