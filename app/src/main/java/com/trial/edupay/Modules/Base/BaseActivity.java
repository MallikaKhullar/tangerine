package com.trial.edupay.Modules.Base;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.view.WindowManager;
        import android.widget.Toast;


/**
 * Created by mallikapriyakhullar on 24/12/17.
 */
public class BaseActivity extends AppCompatActivity{

//    CartItem _cartItem;

//    protected ProgressDialog progressDialog;
//    protected ProgressDialog messageProgressDialog;
//    protected AlertDialog.Builder builder;
//    protected AlertDialog alertDialog;
//
//    protected Boolean isDialogVisible = false;
//    public Typeface font;
//
//    private String feeCycle;
//    private String razorpayKey;

//    private ArrayList<FeeDetail> _feeDetails = new ArrayList<>();
//
//    private PaymentDetail paymentDetail = new PaymentDetail();
//    private FeePaidForStudents feePaidForStudents = new FeePaidForStudents();
//    private UserService _userService = new UserService();
//    PreferenceService preferenceService = new PreferenceService();
//    private StudentService _studentService = new StudentService();

    private static final String TAG = BaseActivity.class.getSimpleName();

    public boolean isConnected;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForMarshmallowPersmission();
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkForInternet();
//    }

    /**
     * for marshmallow devices
     * check if camera and storage permissions are granted, if not then request for it
     */
    public void checkForMarshmallowPersmission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    /**
     * show toast message
     *
     * @param v: message to be dispkayed
     */
    protected void showToast(String v) {
        Toast.makeText(getApplicationContext(), v, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * whenever an error occurs show toast message and also log it in fabric
//     *
//     * @param e: retrofit error
//     */
//    protected void showError(RetrofitError e) {
//        hideProgressBar();
//        Crashlytics.logException(e);
//        if (e != null) {
//            if (e.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                hideProgressBar();
//                showToast(getString(R.string.checkInternet));
//            } else if (e.getResponse() != null) {
//                switch (e.getResponse().getStatus()) {
//                    case 500:
//                        showToast(getString(R.string.tryAgain));
//                        break;
//
//                    case 401:
//                        showToast(getString(R.string.notAuthorised));
//                        break;
//
//                    case 404:
//                        showToast(getString(R.string.infoNotFound));
//                        break;
//
//                    case 403:
//                        showToast(getString(R.string.notAuthorised));
//                        break;
//
//                    default:
//                        showToast("Your request could not be completed because of " + e.getMessage());
//                }
//            } else {
//                showToast(getString(R.string.tryAgain));
//            }
//        }
//    }
//
//    protected void showError(Exception e) {
//        hideProgressBar();
//        Crashlytics.logException(e);
//        showToast("Error processing request, please try again.");
//    }



    /**
     * hide soft key board
     */
    public void hideKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


//
//    public void payFees() {
//
//        if (AppUtil.getEmail() != null && !AppUtil.getEmail().isEmpty() && !AppUtil.getMobile().isEmpty() && AppUtil.getMobile() != null) {
//            addToCart();
//        }
//        else {
//            Intent intent = new Intent(getApplicationContext(), ContactDetailsActivity.class);
//            startActivity(intent);
//        }
//    }

//    public void addToCart(){
//        _cartItem = AppUtil.getCartItem();
//        feeCycle = _cartItem.feeCycle;
//
//        showProgressBar();
//        _userService.addToCart(_cartItem, new Callback<CartItem>() {
//            @Override
//            public void success(CartItem cartItem, Response response) {
//                hideProgressBar();
//                _cartItem = cartItem;
//                AppUtil.saveCartItem(cartItem);
//                getPGKey();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                showError(error);
//            }
//        });
//    }

//    public void getPGKey(){
//        PGKeyParams pgKeyParams = new PGKeyParams();
//        pgKeyParams.organisationId = _cartItem.organisationId;
//        pgKeyParams.amount = _cartItem.amount;
//        showProgressBar();
//        _userService.getPGKey(pgKeyParams, new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                hideProgressBar();
//                razorpayKey = s;
//                startPayment();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                showError(error);
//            }
//        });
//    }


//    public void startPayment() {
//        /**
//         * Replace with your public key
//         */
//
//        final String public_key = razorpayKey;
//        /**
//         * You need to pass current activity in order to let razorpay create CheckoutActivity
//         */
//        final Activity activity = this;
//
//        final Checkout co = new Checkout();
//        co.setPublicKey(public_key);
//
//        try {
//            String string = "{\"color\":\"#EA9116\"}";
//            JSONObject theme = new JSONObject(string);
//
//            JSONObject options = new JSONObject("{" +
//                    "currency: 'INR'}"
//            );
//
//            options.put("theme", theme);
//            options.put("description", "Fee for " + feeCycle);
//
//            options.put("amount", (_cartItem.amount *100)+"");
//            options.put("name", "Fees Payment");
//
//            JSONObject prefillJson = new JSONObject();
//            prefillJson.put("email", AppUtil.getEmail());
//            prefillJson.put("contact", AppUtil.getMobile());
//
//            if(_cartItem.paymentMode.equals("creditCard") || _cartItem.paymentMode.equals("debitCard"))
//                prefillJson.put("method", "card");
//            else if(_cartItem.paymentMode.equals("upi") || _cartItem.paymentMode.equals("wallet"))
//                prefillJson.put("method", _cartItem.paymentMode.toLowerCase());
//
//            options.put("prefill", prefillJson);
//
//
//            JSONObject methodJson = new JSONObject();
//            if(!_cartItem.paymentMode.equals("creditCard") && !_cartItem.paymentMode.equals("debitCard"))
//                methodJson.put("card", false);
//            if(!_cartItem.paymentMode.equals("wallet"))
//                methodJson.put("wallet", false);
//            if(!_cartItem.paymentMode.equals("netBanking"))
//                methodJson.put("netbanking", false);
//            if(!_cartItem.paymentMode.equals("upi"))
//                methodJson.put("upi", false);
//
//            options.put("method", methodJson);
//
//            JSONObject notesJson = new JSONObject();
//            notesJson.put("id", _cartItem.id);
//            options.put("notes", notesJson);
//
//            co.setImage(R.drawable.go_qles_final);
//            co.open(activity, options);
//        } catch (Exception e) {
//            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//
//        if(AppUtil.getAuthKey() == null){
//            preferenceService.setString(PreferenceService.PFEmail, null);
//            preferenceService.setString(PreferenceService.PFMobile, null);
//        }
//    }

//    @Override
//    public void onPaymentSuccess(String s) {
//        try {
//            Intent intent = new Intent(this, PaymentConfirmationActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        } catch (Exception e) {
//            Log.e("com.merchant", e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        try {
//            Toast.makeText(this, "Payment failed: " + s, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, DashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        } catch (Exception e) {
//            Log.e("com.merchant", e.getMessage(), e);
//        }
//    }


//    protected void sendRegistrationToServer() {
//        String token = preferenceService.getString(PreferenceService.PFFcmId);
//        if(token == null)
//            return;
//
//        _userService.sendFcmRegistrationId(token, new Callback<Response>() {
//            @Override
//            public void success(Response response, Response response2) {
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//            }
//        });
//    }

//    public void addStudent(final Student student) {
//        if(student == null)
//            return;
//
//        showProgressBar();
//        _studentService.addStudent(student._id, new Callback<Response>() {
//            @Override
//            public void success(Response response, Response response1) {
//
//                _userService.getUser(new Callback<User>() {
//                    @Override
//                    public void success(User user, Response response) {
//                        hideProgressBar();
//                        showToast(MessageFormat.format("You will get reminders for {0} fees.", student.name));
//                        goToHomePage();
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        showError(error);
//                    }
//                });
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                showError(error);
//            }
//        });
//    }
}
