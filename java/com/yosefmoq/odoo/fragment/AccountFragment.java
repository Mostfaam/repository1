package com.yosefmoq.odoo.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yosefmoq.odoo.BuildConfig;
import com.yosefmoq.odoo.R;
import com.yosefmoq.odoo.activity.BaseActivity;
import com.yosefmoq.odoo.activity.ContactUsActivity;
import com.yosefmoq.odoo.activity.HomeActivity;
import com.yosefmoq.odoo.activity.SettingsActivity;
import com.yosefmoq.odoo.activity.SignInSignUpActivity;
import com.yosefmoq.odoo.connection.ApiConnection;
import com.yosefmoq.odoo.connection.CustomObserver;
import com.yosefmoq.odoo.database.SqlLiteDbHelper;
import com.yosefmoq.odoo.databinding.FragmentAccountBinding;
import com.yosefmoq.odoo.dialog_frag.ProfilePictureDialogFragment;
import com.yosefmoq.odoo.handler.customer.AccountFragmentHandler;
import com.yosefmoq.odoo.helper.AlertDialogHelper;
import com.yosefmoq.odoo.helper.AppSharedPref;
import com.yosefmoq.odoo.helper.Helper;
import com.yosefmoq.odoo.helper.ImageHelper;
import com.yosefmoq.odoo.helper.SnackbarHelper;
import com.yosefmoq.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.yosefmoq.odoo.model.home.HomePageResponse;
import com.yosefmoq.odoo.model.request.SaveCustomerDetailRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yosefmoq.odoo.activity.HomeActivity.RC_SIGN_IN_SIGN_UP;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;
import static com.yosefmoq.odoo.helper.ImageHelper.encodeImage;

import java.util.HashMap;
import java.util.Map;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class AccountFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "AccountFragment";
    public FragmentAccountBinding mBinding;

    public static AccountFragment newInstance(HomePageResponse homePageResponse) {
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        AccountFragment accountFragment = new AccountFragment();
        accountFragment.setArguments(args);
        return accountFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mBinding.customerProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppSharedPref.getCustomerProfileImage(getContext()).trim().isEmpty())
                    showEnlargedProfileImage();
            }
        });
        if(AppSharedPref.isLoggedIn(requireContext())){
            mBinding.clNoLogin.setVisibility(View.GONE);
            mBinding.svLogin.setVisibility(View.VISIBLE);
            mBinding.clToolbar.setVisibility(View.GONE);
        }else{
            mBinding.clNoLogin.setVisibility(View.VISIBLE);
            mBinding.svLogin.setVisibility(View.GONE);
            mBinding.clToolbar.setVisibility(View.VISIBLE);

        }
        mBinding.tvAccSignIn.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(),SignInSignUpActivity.class);
            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, HomeActivity.class.getSimpleName());
            startActivityForResult(i, RC_SIGN_IN_SIGN_UP);
        });
        mBinding.tvAccountRate1.setOnClickListener(v -> {
            mBinding.tvAccountRate.performClick();
        });
        mBinding.tvAccountRate.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Uri uri = Uri.parse("market://details?id=" + requireContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    requireContext().startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + requireContext().getPackageName())));
                }
            } else {
                requireContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + requireContext().getPackageName())));
            }
        });

        mBinding.tvAccShare1.setOnClickListener(v -> {
            mBinding.tvAccShare.performClick();
        });
        mBinding.tvAccShare.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });
        mBinding.tvAccSettings1.setOnClickListener(v -> {
            mBinding.tvAccSettings.performClick();
        });
        mBinding.tvAccSettings.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });
        return mBinding.getRoot();
    }

    public void showEnlargedProfileImage() {
        ProfilePictureDialogFragment dialogFragment = new ProfilePictureDialogFragment();
        dialogFragment.show(getFragmentManager(), getTag());

    }

    //For Enlarge a view using a zoom animation for profile picture
/*    private void zoomImageFromThumb(final View thumbView) {
        int mShortAnimationDuration = 1000;
        final Animator[] mCurrentAnimator = {null};
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator[0] != null) {
            mCurrentAnimator[0].cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = mBinding.expandedImage;
        ImageHelper.load(expandedImageView, AppSharedPref.getCustomerProfileImage(getContext()), R.drawable.com_facebook_profile_picture_blank_square, null, true, null);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        mBinding.getRoot().findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator[0] = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator[0] = null;
            }
        });
        set.start();
        mCurrentAnimator[0] = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator[0] != null) {
                    mCurrentAnimator[0].cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator[0] = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator[0] = null;
                    }
                });
                set.start();
                mCurrentAnimator[0] = set;
            }
        });
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setWishlistEnabled(AppSharedPref.isAllowedWishlist(getActivity()));
        Helper.hideKeyboard(getContext());
        mBinding.contactUs.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), ContactUsActivity.class));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.setHandler(new AccountFragmentHandler(getContext(), this));

        mBinding.tvAccLang1.setOnClickListener(v -> {
            mBinding.tvAccLang.performClick();
        });
        mBinding.tvAccLang.setOnClickListener(v -> {
            HomePageResponse homePageResponse = getArguments().getParcelable(BUNDLE_KEY_HOME_PAGE_RESPONSE);

            onClickLanguageIcon(homePageResponse.getLanguageMap());
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.setCustomerName(AppSharedPref.getCustomerName(getContext()));
        mBinding.setEmail(AppSharedPref.getCustomerEmail(getContext()));
        mBinding.setIsEmailVerified(AppSharedPref.isEmailVerified(getContext()));
    }

    public void uploadFile(final Uri selectedImageUri, boolean isFromCropImage) {

        String filePath = "";
        if (!isFromCropImage){

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContext().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
        if (cursor == null) {
            SnackbarHelper.getSnackbar((Activity) getContext(), getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            return;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
        cursor.close();
        }else {
            filePath = selectedImageUri.getPath();
        }
        SaveCustomerDetailRequest saveCustomerDetailRequest;
        if (mBinding.getHandler().isRequestForProfileImage()){
            saveCustomerDetailRequest = new SaveCustomerDetailRequest(null,null,encodeImage(filePath),null);
        }else {
            saveCustomerDetailRequest = new SaveCustomerDetailRequest(null,null,null,encodeImage(filePath));
        }


        if (!TextUtils.isEmpty(filePath)){
            ApiConnection.saveCustomerDetails(getContext(),saveCustomerDetailRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SaveCustomerDetailResponse>(getContext()) {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(getContext());
                }

                @Override
                public void onNext(@io.reactivex.annotations.NonNull SaveCustomerDetailResponse saveCustomerDetailResponse) {
                    super.onNext(saveCustomerDetailResponse);
                    if (saveCustomerDetailResponse.isAccessDenied()){
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(getContext());
                                Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                                startActivity(i);
                            }
                        });
                    }else {
                        if (saveCustomerDetailResponse.isSuccess()) {
                            mBinding.executePendingBindings();
                            SnackbarHelper.getSnackbar((Activity) getContext(), saveCustomerDetailResponse.getMessage(), Snackbar.LENGTH_SHORT).show();
                            if (mBinding.getHandler().isRequestForProfileImage()){
                                AppSharedPref.setCustomerProfileImage(getContext(), saveCustomerDetailResponse.getCustomerProfileImage());
                                ImageHelper.load(mBinding.customerProfileImage, saveCustomerDetailResponse.getCustomerProfileImage(), R.drawable.ic_men_avatar, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.PROFILE_PIC_GENERIC);

                            }else {
                                AppSharedPref.setCustomerBannerImage(getContext(), saveCustomerDetailResponse.getCustomerBannerImage());
//                                ImageHelper.load(mBinding.profileBanner, saveCustomerDetailResponse.getCustomerBannerImage(),null, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.BANNER_SIZE_LARGE);

                            }
                        } else {
                            AlertDialogHelper.showDefaultWarningDialog(getContext(), getContext().getString(R.string.error_something_went_wrong), saveCustomerDetailResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        }else {
            SnackbarHelper.getSnackbar((Activity) getContext(), getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }


    }


    public void updateProfileImageAfterDelete(){
        if (mBinding.getHandler().isRequestForProfileImage()){
            ImageHelper.load(mBinding.customerProfileImage, "", R.drawable.ic_men_avatar, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.PROFILE_PIC_GENERIC);

        }else {
//            ImageHelper.load(mBinding.profileBanner, "", null, DiskCacheStrategy.NONE, true, ImageHelper.ImageType.BANNER_SIZE_LARGE);

        }
    }
    public void onClickLanguageIcon(HashMap languageMap) {
        if (languageMap.size() > 0) {
            RadioGroup group = new RadioGroup(requireContext());
            group.setOrientation(LinearLayout.VERTICAL);
            group.setPadding(20, 20, 20, 20);
            HashMap<String, String> map = languageMap;
            for (Map.Entry<String, String> pair : map.entrySet()) {
                RadioButton button = new RadioButton(requireContext());
                button.setText(pair.getValue());
                button.setTag(pair.getKey());
                group.addView(button);
                if (pair.getKey().equals(AppSharedPref.getLanguageCode(requireContext()))) {
                    group.check(button.getId());
                }
            }

            AlertDialog languageDialog = new AlertDialog.Builder(requireContext()).setTitle(R.string.language)
                    .setView(group).setCancelable(true).create();
            group.setOnCheckedChangeListener((group1, checkedId) -> {
                RadioButton selectedButton = group1.findViewById(checkedId);
                String selectedLang = (String) selectedButton.getTag();
                if (!selectedLang.equals(AppSharedPref.getLanguageCode(requireContext()))) {
                    AppSharedPref.setLanguageCode(requireContext(), selectedLang);
                    AppSharedPref.setIsLanguageChange(requireContext(), true);
                    // delete product data

                    SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(requireContext());
                    sqlLiteDbHelper.deleteAllProductData();

                    BaseActivity.setLocale(requireContext(), true);
                }
            });

            languageDialog.show();
        }
    }
}
