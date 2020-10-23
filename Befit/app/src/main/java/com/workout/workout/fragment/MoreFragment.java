package com.workout.workout.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.Builder;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.workout.workout.R;
import com.workout.workout.activity.LoginActivity;
import com.workout.workout.activity.PhoneAuthenticationActivity;
import com.workout.workout.activity.PremiumVersion;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.managers.UserManager;
import com.workout.workout.modal.User;
import com.workout.workout.util.AppUtil;
import java.util.List;

public class MoreFragment extends Fragment implements PurchasesUpdatedListener, OnConnectionFailedListener, OnClickListener {
    private static final int RC_SIGN_IN = 1;
    private Button buttonLoginWithEmail;
    private Button buttonLoginWithPhoneNumber;
    private Button buttonPremium;
    private Button buttonRemoveAds;
    private Button buttonSignout;
    private CardView cardViewProfile;
    private ImageView imageViewFacebook;
    private ImageView imageViewGooglePlus;
    private ImageView imageViewLinkedIn;
    private ImageView imageViewTwitter;
    private ImageView imageViewUser;
    private ImageView imageViewYoutube;
    private boolean isEmailOrPhoneSigninProcessInitiated;
    private boolean isGoogleSigninProcessInitiated;
    private LinearLayout linearLayoutAuth;
    private FirebaseAuth mAuth;
    private AuthStateListener mAuthListener;
    private BillingClient mBillingClient;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar progressBar;
    private SignInButton signInButton;
    private TextView textViewUserEmail;
    private TextView textViewUserName;
    private TextView textViewUserPhone;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        preparebillingClient();
        initializeView(view);
        hangleGoogleSignIn();
//        if (PersistenceManager.isUserSignedIn()) {
//            String name = PersistenceManager.getDisplayName();
//            String email = PersistenceManager.getEmail();
//            String phoneNumber = PersistenceManager.getPhoneNumber();
//            String profilePicUrl = PersistenceManager.getPhotoUrl();
//            String providerId = PersistenceManager.getProviderId();
//            this.textViewUserName.setText(name);
//            if (AppUtil.isEmpty(phoneNumber)) {
//                this.textViewUserPhone.setVisibility(View.GONE);
//            } else {
//                this.textViewUserPhone.setText(phoneNumber);
//                this.textViewUserPhone.setVisibility(View.VISIBLE);
//            }
//            if (AppUtil.isEmpty(email)) {
//                this.textViewUserEmail.setVisibility(View.GONE);
//            } else {
//                this.textViewUserEmail.setText(email);
//                this.textViewUserEmail.setVisibility(View.VISIBLE);
//            }
//            if (AppUtil.isEmpty(profilePicUrl)) {
//                Glide.with(getActivity()).load(Integer.valueOf(R.drawable.ic_user)).placeholder((int) R.drawable.ic_user).into(this.imageViewUser);
//            } else {
//                Glide.with(getActivity()).load(profilePicUrl).placeholder((int) R.drawable.ic_user).into(this.imageViewUser);
//            }
//            hideAuthView();
//            showProfileView();
//        } else {
//            hideProfileView();
//            showAuthView();
//        }
        hideProfileView();
        showAuthView();
        return view;
    }

    private void preparebillingClient() {
        this.mBillingClient =  BillingClient.newBuilder(getActivity()).setListener(this).build();
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(int billingResponseCode) {
                if (billingResponseCode != 0) {
                }
            }

            public void onBillingServiceDisconnected() {
            }
        });
    }

    public void onResume() {
        super.onResume();
        if (this.isGoogleSigninProcessInitiated) {
            this.progressBar.setVisibility(View.VISIBLE);
        } else if (!this.isEmailOrPhoneSigninProcessInitiated) {
            this.progressBar.setVisibility(View.GONE);
        }
//        String email = PersistenceManager.getEmail();
//        String phoneNumber = PersistenceManager.getPhoneNumber();
        String email = "";
        String phoneNumber = "";
        if (this.isGoogleSigninProcessInitiated || !this.isEmailOrPhoneSigninProcessInitiated) {
            if (AppUtil.isEmpty(email)) {
                this.textViewUserEmail.setVisibility(View.GONE);
            } else {
                this.textViewUserEmail.setText(email);
                this.textViewUserEmail.setVisibility(View.VISIBLE);
            }
            if (AppUtil.isEmpty(phoneNumber)) {
                this.textViewUserPhone.setVisibility(View.GONE);
            } else {
                this.textViewUserPhone.setText(phoneNumber);
                this.textViewUserPhone.setVisibility(View.VISIBLE);
            }
        } else {
            this.progressBar.setVisibility(View.GONE);
            this.isEmailOrPhoneSigninProcessInitiated = false;
            if (PersistenceManager.isUserSignedIn()) {
                String name = PersistenceManager.getDisplayName();
                String profilePicUrl = PersistenceManager.getPhotoUrl();
                String providerId = PersistenceManager.getProviderId();
                this.textViewUserName.setText(name);
                if (AppUtil.isEmpty(email)) {
                    this.textViewUserEmail.setVisibility(View.GONE);
                } else {
                    this.textViewUserEmail.setText(email);
                    this.textViewUserEmail.setVisibility(View.VISIBLE);
                }
                if (AppUtil.isEmpty(phoneNumber)) {
                    this.textViewUserPhone.setVisibility(View.GONE);
                } else {
                    this.textViewUserPhone.setText(phoneNumber);
                    this.textViewUserPhone.setVisibility(View.VISIBLE);
                }
                if (AppUtil.isEmpty(profilePicUrl)) {
                    Glide.with(getActivity()).load(Integer.valueOf(R.drawable.ic_user)).placeholder((int) R.drawable.ic_user).into(this.imageViewUser);
                } else {
                    Glide.with(getActivity()).load(profilePicUrl).placeholder((int) R.drawable.ic_user).into(this.imageViewUser);
                }
                hideAuthView();
                showProfileView();
            } else {
                hideProfileView();
                showAuthView();
            }
        }
//        if (PersistenceManager.isAdsFreeVersion()) {
//            this.buttonRemoveAds.setVisibility(View.GONE);
//        } else {
//            this.buttonRemoveAds.setVisibility(View.VISIBLE);
//        }
//        if (PersistenceManager.isPremiumVersion()) {
//            this.buttonPremium.setText("Get Trainer Guidance");
//        } else {
//            this.buttonPremium.setVisibility(View.VISIBLE);
//        }
    }

    private void initializeView(View view) {
        this.signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        this.signInButton.setOnClickListener(this);
        this.buttonLoginWithEmail = (Button) view.findViewById(R.id.buttonLoginWithEmail);
        this.buttonLoginWithPhoneNumber = (Button) view.findViewById(R.id.buttonLoginWithPhoneNumber);
        this.linearLayoutAuth = (LinearLayout) view.findViewById(R.id.linearLayoutAuth);
        this.cardViewProfile = (CardView) view.findViewById(R.id.cardViewProfile);
        this.imageViewUser = (ImageView) view.findViewById(R.id.imageViewUser);
        this.textViewUserName = (TextView) view.findViewById(R.id.textViewUserName);
        this.textViewUserEmail = (TextView) view.findViewById(R.id.textViewUserEmail);
        this.buttonSignout = (Button) view.findViewById(R.id.buttonSignout);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        this.textViewUserPhone = (TextView) view.findViewById(R.id.textViewUserPhone);
        this.imageViewFacebook = (ImageView) view.findViewById(R.id.imageViewFacebook);
        this.imageViewGooglePlus = (ImageView) view.findViewById(R.id.imageViewGooglePlus);
        this.imageViewLinkedIn = (ImageView) view.findViewById(R.id.imageViewLinkedIn);
        this.imageViewTwitter = (ImageView) view.findViewById(R.id.imageViewTwitter);
        this.imageViewYoutube = (ImageView) view.findViewById(R.id.imageViewYoutube);
        this.buttonRemoveAds = (Button) view.findViewById(R.id.buttonRemoveAds);
        this.buttonPremium = (Button) view.findViewById(R.id.buttonPremium);
        this.imageViewFacebook.setOnClickListener(this);
        this.imageViewGooglePlus.setOnClickListener(this);
        this.imageViewLinkedIn.setOnClickListener(this);
        this.imageViewTwitter.setOnClickListener(this);
        this.imageViewYoutube.setOnClickListener(this);
        this.buttonLoginWithEmail.setOnClickListener(this);
        this.buttonLoginWithPhoneNumber.setOnClickListener(this);
        this.buttonSignout.setOnClickListener(this);
        this.buttonRemoveAds.setOnClickListener(this);
        this.buttonPremium.setOnClickListener(this);
    }

    private void hangleGoogleSignIn() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()).build();
        this.mAuth = FirebaseAuth.getInstance();
    }

    private void showProfileView() {
        this.cardViewProfile.setVisibility(View.VISIBLE);
        Log.w("ContentValues", "showProfileView");
    }

    private void hideProfileView() {
        this.cardViewProfile.setVisibility(View.GONE);
        Log.w("ContentValues", "hideProfileView");
    }

    private void showAuthView() {
        this.linearLayoutAuth.setVisibility(View.VISIBLE);
        this.buttonLoginWithEmail.setVisibility(View.VISIBLE);
        this.buttonLoginWithPhoneNumber.setVisibility(View.VISIBLE);
        this.signInButton.setVisibility(View.VISIBLE);
        Log.w("ContentValues", "showAuthView");
    }

    private void hideAuthView() {
        this.linearLayoutAuth.setVisibility(View.GONE);
        this.buttonLoginWithEmail.setVisibility(View.GONE);
        this.buttonLoginWithPhoneNumber.setVisibility(View.GONE);
        this.signInButton.setVisibility(View.GONE);
        Log.w("ContentValues", "hideAuthView");
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (isAdded()) {
            Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignout:
                handleSignout();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Signout click"));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            case R.id.sign_in_button:
                signIn();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Google Login click"));
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            case R.id.buttonLoginWithEmail:
                handleLoginWithEmail();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Email Login click"));
                    return;
                } catch (Exception e22) {
                    e22.printStackTrace();
                    return;
                }
            case R.id.buttonLoginWithPhoneNumber:
                handleLoginWithPhoneNumber();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Phone login click"));
                    return;
                } catch (Exception e222) {
                    e222.printStackTrace();
                    return;
                }
            case R.id.buttonRemoveAds:
                handleRemoveAds();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Remove Ads"));
                    return;
                } catch (Exception e2222) {
                    e2222.printStackTrace();
                    return;
                }
            case R.id.buttonPremium:
                handlePremium();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Premium click"));
                    return;
                } catch (Exception e22222) {
                    e22222.printStackTrace();
                    return;
                }
            case R.id.imageViewGooglePlus:
                handleGooglePlusClick();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Google plus click"));
                    return;
                } catch (Exception e222222) {
                    e222222.printStackTrace();
                    return;
                }
            case R.id.imageViewFacebook:
                handleFacebookClick();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Facebook click"));
                    return;
                } catch (Exception e2222222) {
                    e2222222.printStackTrace();
                    return;
                }
            case R.id.imageViewTwitter:
                handleTwitterClick();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Twitter click"));
                    return;
                } catch (Exception e22222222) {
                    e22222222.printStackTrace();
                    return;
                }
            case R.id.imageViewLinkedIn:
                handleLinkedInClick();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Linkedin click"));
                    return;
                } catch (Exception e222222222) {
                    e222222222.printStackTrace();
                    return;
                }
            case R.id.imageViewYoutube:
                handleYoutubeClick();
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Youtube click"));
                    return;
                } catch (Exception e2222222222) {
                    e2222222222.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    private void handlePremium() {
        if (PersistenceManager.isPremiumVersion()) {
            Intent email = new Intent("android.intent.action.SEND");
            email.putExtra("android.intent.extra.EMAIL", new String[]{"progymworkout@gmail.com"});
            email.putExtra("android.intent.extra.SUBJECT", "Premium Trainer Guidance");
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email sender :"));
            return;
        }
        try {
            startActivity(new Intent(getActivity(), PremiumVersion.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRemoveAds() {
        if (!PersistenceManager.isAdsFreeVersion()) {
            BillingFlowParams.Builder builder = BillingFlowParams.newBuilder().setSku(AppConstants.SKU_AD_FREE).setType("inapp");
            if (this.mBillingClient != null) {
                this.mBillingClient.launchBillingFlow(getActivity(), builder.build());
            }
        } else if (isAdded()) {
            Toast.makeText(getActivity(), "Ads are already removed!", Toast.LENGTH_LONG).show();
        }
    }

    private void handleSignout() {
        FirebaseAuth.getInstance().signOut();
        if (this.mGoogleApiClient != null && this.mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(this.mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                public void onResult(@NonNull Status status) {
                    MoreFragment.this.hideProfileView();
                    MoreFragment.this.showAuthView();
                    PersistenceManager.resetUserProfile();
                }
            });
        }
    }

    private void handleLoginWithPhoneNumber() {
        startActivity(new Intent(getActivity(), PhoneAuthenticationActivity.class));
        this.progressBar.setVisibility(View.VISIBLE);
        this.isEmailOrPhoneSigninProcessInitiated = true;
    }

    private void handleLoginWithEmail() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        this.progressBar.setVisibility(View.VISIBLE);
        this.isEmailOrPhoneSigninProcessInitiated = true;
    }

    private void signIn() {
        if (this.mGoogleApiClient != null) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.isGoogleSigninProcessInitiated = true;
            startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(this.mGoogleApiClient), 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            handleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("ContentValues", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
            try {
                Answers.getInstance().logSignUp((SignUpEvent) ((SignUpEvent) ((SignUpEvent) new SignUpEvent().putMethod("Firebase Auth With Google").putSuccess(true).putCustomAttribute("display name", account != null ? account.getDisplayName() : "null")).putCustomAttribute("email", account != null ? account.getEmail() : "null")).putCustomAttribute("ID", account != null ? account.getId() : "null"));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        PersistenceManager.resetUserProfile();
        Log.w("ContentValues", "signInWithCredential:failure" + result.getStatus().getStatusMessage());
        if (isAdded()) {
            Toast.makeText(getActivity(), "Some error occurred", Toast.LENGTH_SHORT).show();
        }
        showAuthView();
        hideProfileView();
        this.isGoogleSigninProcessInitiated = false;
        this.progressBar.setVisibility(View.GONE);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("ContentValues", "firebaseAuthWithGoogle:" + acct.getId());
        this.mAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.getIdToken(), null)).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("ContentValues", "signInWithCredential:success");
                    FirebaseUser user = MoreFragment.this.mAuth.getCurrentUser();
                    Uri profileUrl = user.getPhotoUrl();
                    String token = acct.getIdToken();
                    String uid = user.getUid();
                    PersistenceManager.setDisplayName(user.getDisplayName());
                    PersistenceManager.setEmail(user.getEmail());
                    PersistenceManager.setUserUid(user.getUid());
                    if (profileUrl != null) {
                        PersistenceManager.setPhotoUrl(profileUrl.toString());
                    }
                    PersistenceManager.setProviderId((String) user.getProviders().get(0));
                    PersistenceManager.setPhoneNumber(user.getPhoneNumber());
                    PersistenceManager.setUserSignedIn(true);
                    if (MoreFragment.this.isAdded()) {
                        Toast.makeText(MoreFragment.this.getActivity(), "Successfully signed in", Toast.LENGTH_SHORT).show();
                    }
                    MoreFragment.this.hideAuthView();
                    MoreFragment.this.showProfileView();
                    String name = PersistenceManager.getDisplayName();
                    String email = PersistenceManager.getEmail();
                    String phoneNumber = PersistenceManager.getPhoneNumber();
                    String profilePicUrl = PersistenceManager.getPhotoUrl();
                    String providerId = PersistenceManager.getProviderId();
                    MoreFragment.this.textViewUserName.setText(name);
                    if (AppUtil.isEmpty(email)) {
                        MoreFragment.this.textViewUserEmail.setVisibility(View.GONE);
                    } else {
                        MoreFragment.this.textViewUserEmail.setVisibility(View.VISIBLE);
                    }
                    MoreFragment.this.textViewUserEmail.setText(email);
                    if (AppUtil.isEmpty(profilePicUrl)) {
                        Glide.with(MoreFragment.this.getActivity()).load(Integer.valueOf(R.drawable.ic_user)).placeholder((int) R.drawable.ic_user).into(MoreFragment.this.imageViewUser);
                    } else {
                        Glide.with(MoreFragment.this.getActivity()).load(profilePicUrl).placeholder((int) R.drawable.ic_user).into(MoreFragment.this.imageViewUser);
                    }
                    MoreFragment.this.hideAuthView();
                    MoreFragment.this.showProfileView();
                    User localUser = new User("","","","","","","","");
                    localUser.setDisplay_name(name);
                    localUser.setEmail(email);
                    localUser.setIdentifier("");
                    localUser.setMobile_number(phoneNumber);
                    localUser.setUser_uid(uid);
                    localUser.setToken(token);
                    localUser.setProvider_id(providerId);
                    localUser.setPhoto_url(profilePicUrl);
                    UserManager.createNewUserInFirebaseDB(localUser);
                } else {
                    Log.w("ContentValues", "signInWithCredential:failure", task.getException());
                    PersistenceManager.resetUserProfile();
                    if (MoreFragment.this.isAdded()) {
                        Toast.makeText(MoreFragment.this.getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                    MoreFragment.this.showAuthView();
                    MoreFragment.this.hideProfileView();
                }
                MoreFragment.this.isGoogleSigninProcessInitiated = false;
                MoreFragment.this.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void onStop() {
        super.onStop();
        if (this.mGoogleApiClient != null) {
            this.mGoogleApiClient.stopAutoManage(getActivity());
            this.mGoogleApiClient.disconnect();
        }
    }

    private void handleGooglePlusClick() {
        String profile = "116150114479359099390";
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", profile);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/" + profile)));
        }
    }

    private void handleFacebookClick() {
        Intent intent = null;
        Intent browserIntent = null;
        try {
            getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent("android.intent.action.VIEW", Uri.parse("fb://page/113804209289999"));
        } catch (Exception e) {
            browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/Pro-Gym-Workout-113804209289999"));
        }
        if (intent != null) {
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                if (browserIntent != null) {
                    startActivity(browserIntent);
                }
            }
        }
    }

    private void handleLinkedInClick() {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("linkedin://13403532"));
        if (getContext().getPackageManager().queryIntentActivities(intent, 65536).isEmpty()) {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/company/13403532/"));
        }
        startActivity(intent);
    }

    private void handleTwitterClick() {
        Intent intent;
        PackageInfo info = null;
        try {
            info = getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info == null || !info.applicationInfo.enabled) {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("https://twitter.com/progymworkout"));
        } else {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("twitter://user?user_id=902036387716710400"));
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void handleYoutubeClick() {
        Intent applicationIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.youtube.com/channel/UCIyhF9hCENv_pb1r2qKxVhQ"));
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.youtube.com/channel/UCIyhF9hCENv_pb1r2qKxVhQ"));
        try {
            startActivity(applicationIntent);
        } catch (ActivityNotFoundException e) {
            if (browserIntent != null) {
                startActivity(browserIntent);
            }
        }
    }

    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
        if (responseCode == 0 && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getSku().equalsIgnoreCase(AppConstants.SKU_AD_FREE)) {
                    if (isAdded()) {
                        Toast.makeText(getActivity(), "All ads removed!", Toast.LENGTH_LONG).show();
                    }
                    PersistenceManager.setAdsFreeVersion(true);
                }
            }
        } else if (responseCode != 1 && responseCode != -1 && responseCode != 5 && responseCode != 3 && responseCode != 6 && responseCode != -2 && responseCode != 7 && responseCode != 2 && responseCode == 4) {
        }
    }
}
