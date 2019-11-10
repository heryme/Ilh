package com.activityhub.utils.other;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {


    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@&_#*!]).$";

    public static boolean validateEmailNotNull(String email) {

        if (email.trim().length() == 0)
            return false;

        return true;
    }

    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
        //return true;
    }

    public static boolean validPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matche = pattern.matcher(password);
        return matche.matches();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

//        final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
//        final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*[A-Z])(?=.*[@&_#*!]))";
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@&_#*!]).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static boolean isBothPasswordEqual(Context context, EditText edtText1, EditText edtText2) {
        boolean isValid = true;
        String str1 = edtText1.getText().toString().trim();
        String str2 = edtText2.getText().toString().trim();
        if (!str1.equals(str2)) {
            isValid = false;
            edtText2.requestFocus();
            edtText2.setFocusable(true);
           // edtText2.setError(context.getString(R.string.err_msg_re_enter_password_dosnt_match));

        }

        return isValid;
    }

    public static boolean validatePasswordNotNull(String password) {

        if (password.trim().length() == 0)
            return false;

        return true;
    }

    public static boolean validateBirthdateNotNull(String birthdate) {

        if (birthdate.trim().length() == 0)
            return false;

        return true;
    }

    public static boolean validateGenderNotNull(String gender) {

        if (gender.trim().length() == 0)
            return false;

        return true;
    }

    public static boolean validateNameNotNull(String name) {
        if (name.trim().length() == 0)
            return false;
        return true;
    }

    public static boolean validateAddressNotNull(String address) {
        if (address.trim().length() == 0)
            return false;
        return true;
    }

	/*public static LatLng addressToLocation(Context context, String address)
	{
        try
        {
	        List<Address> foundGeocode = null;
	        foundGeocode = new Geocoder(context).getFromLocationName(address, 1);
	        return new LatLng(foundGeocode.get(0).getLatitude(), foundGeocode.get(0).getLongitude());
        }
        catch (Exception e)
        {
        	return new LatLng(0, 0);
        }
	}*/

    public static boolean validateAddress(String address) {

        return false;
    }

    public static String getFacebookHashKey(Context c) {
        String key = "";
        // Add code to print out the key hash
        try {
            PackageInfo info = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("FB KeyHash:---", key);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return key;
    }

    public static String getDateFormatedTime(String time) {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = (Date) formatter.parse(time);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
            return newFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }
}