package hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.InAppActivity;

public class Security {
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String TAG = "IABUtil/Security";

    //    public static boolean verifyPurchase(String base64PublicKey, String signedData, String signature) {
//        if (!TextUtils.isEmpty(signedData) && !TextUtils.isEmpty(base64PublicKey) && !TextUtils.isEmpty(signature)) {
//            return verify(generatePublicKey(base64PublicKey), signedData, signature);
//        }
//        Log.e(TAG, "Purchase verification failed: missing data.");
//        return false;
//    }
    public static boolean verifyPurchase( String base64PublicKey,
                                         String signedData, String signature) {
        String productId = InAppActivity.SKU_REMOVE_ADS;
        if (TextUtils.isEmpty(signedData) || TextUtils.isEmpty(base64PublicKey) ||
                TextUtils.isEmpty(signature)) {

            if (
                    productId.equals("android.test.purchased") ||
                            productId.equals("android.test.canceled") ||
                            productId.equals("android.test.refunded") ||
                            productId.equals("android.test.item_unavailable")
                    ) {
                return true;
            }

            Log.e(TAG, "Purchase verification failed: missing data.");
            return false;
        }

        PublicKey key = Security.generatePublicKey(base64PublicKey);
        return Security.verify(key, signedData, signature);
    }

    public static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            return KeyFactory.getInstance(KEY_FACTORY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode(encodedPublicKey, Base64.DEFAULT)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e2) {
            Log.e(TAG, "Invalid key specification.");
            throw new IllegalArgumentException(e2);
        }
    }

    public static boolean verify(PublicKey publicKey, String signedData, String signature) {
        try {
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            if (sig.verify(Base64.decode(signature, Base64.DEFAULT))) {
                return true;
            }
            Log.e(TAG, "Signature verification failed.");
            return false;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "NoSuchAlgorithmException.");
            return false;
        } catch (InvalidKeyException e2) {
            Log.e(TAG, "Invalid key specification.");
            return false;
        } catch (SignatureException e3) {
            Log.e(TAG, "Signature exception.");
            return false;
        }
    }
}
