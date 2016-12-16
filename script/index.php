<?php
	$dir = dirmane(__FILE__).'/cookietemp';
	
	$cookie_path=$dir.'/cookie.txt';
	//Build Name Value Pairs
	$post = 's-libapps-email=sterbec1@tcnj.edu&s-libapps-password=abcd1234';
        $url = 'https://tcnj.libapps.com/libapps/login.php';
        date_default_timezone_set('America/New_York');

        //init curl & set options for login
        $ch = curl_init();

        //set url, follow redirects, accept all certificates, etc.
        $options = array(
                CURLOPT_URL => $url,
                CURLOPT_RETURNTRANSFER => TRUE,
                CURLOPT_NOPROGRESS => FALSE,
                CURLOPT_HEADER_OUT => TRUE,
                CURLOPT_FOLLOWLOCATION => FALSE,
                CURLOPT_SSL_VERIFYPEER => FALSE,
                CURLOPT_POST => TRUE,
                CURLOPT_POSTFIELDS => $post,
                CURLOPT_REFERER => $_SERVER['REQUEST_URI'],
                CURLOPT_SSL_VERIFYPEER => FALSE,
                CURLOPT_COOKIESESSION => TRUE, //store cookie to save login session
                CURLOPT_COOKIEJAR => $cookie_path,
                CURLOPT_COOKIEFILE => $cookie_path
                );
        curl_setopt_array($ch, $options);


        //execute curl requst & check for errors
        $output = curl_exec($ch);

        echo curl_getinfo($ch, CURLINFO_HEADER_OUT);

        if ($output === FALSE) {
                echo "cURL Returned an Error: " . curl_getinfo($ch);
        }

        //echo curl_getinfo($ch, CURLOPT_URL);

        curl_setopt(CURLOPT_URL => 'https://tcnj.libinsight.com/dataseta.php?id=3358'); //update url
        $post='from=Jan 01 2015&to='.date('%M %d %Y'); //Start date to Current Date full 24 hours
        curl_setopt(CURLOPT_POSTFIELDS => $post); //update post info

        //execute request & check errors
        $output = curl_exec($ch);

        if ($output === FALSE) {
                echo "cURL Returned an Error: " . curl_errno($ch);
        }

        //create file
        $dir=dirname(__FILE).'/roomdat';
        $path=$dir.'roomdat.json';

        //put output data into /roomdat/roomdat.json
        file_put_contents($path, $output);

        curl_close($ch);

?>
