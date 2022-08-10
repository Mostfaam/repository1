//package com.yosefmoq.odoo.activity
//
//import android.app.ActivityManager
//import android.content.Context
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.view.MotionEvent
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.databinding.DataBindingUtil
//import com.google.android.material.snackbar.Snackbar
//import com.yosefmoq.odoo.R
//import com.yosefmoq.odoo.databinding.ActivityArBinding
//import com.yosefmoq.odoo.helper.SnackbarHelper
//
//class ARActivity : BaseActivity() {
//    private lateinit var mBinding: ActivityArBinding
//    private lateinit var arFragment: ArFragment
//    private var anchorNode: AnchorNode? = null
//    private var objectRenderable: ModelRenderable? = null
//    private lateinit var modelUrl: String
//    private lateinit var snackbar: Snackbar
//
//    private fun checkIsSupportedDeviceOrFinish(): Boolean {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            Log.e("error", "Sceneform requires Android N or later")
//            return false
//        }
//        val openGlVersionString = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
//                .deviceConfigurationInfo
//                .glEsVersion
//        if (java.lang.Double.parseDouble(openGlVersionString) < 3.1) {
//            Log.e("error", "Sceneform requires OpenGL ES 3.1 later")
//            return false
//        }
//        return true
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // setContentView(R.layout.activity_a_r)
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ar)
//        // mBinding.handler = ARActivityHandler(this)
//
//        modelUrl = intent.getStringExtra("ar_android").toString()
//
//        //Toast.makeText(this, modelUrl, Toast.LENGTH_SHORT).show()
//
//        // Demo Gltf URL
//        // Comment this
//        //modelUrl = "https://github.com/KhronosGroup/glTF-Sample-Models/blob/master/2.0/Duck/glTF/Duck.gltf"
//
//        if (checkIsSupportedDeviceOrFinish()) {
//            if (modelUrl.isNotBlank()) {
//                startARSession()
//            }
//        } else {
//            this.finish()
//            // Model URL is not valid
//            // TODO("Add required code in case Model URL is not valid")
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun startARSession() {
//        try {
//            arFragment = mSupportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
//            snackbar = SnackbarHelper.getSnackbar(this, getString(R.string.downloading_model), Snackbar.LENGTH_INDEFINITE)
//            // Toast.makeText(this, getString(R.string.downloading_model), Toast.LENGTH_SHORT).show()
//            snackbar.show()
//            // Init renderable
//            loadModel()
//
//            // Set tap listener
//            arFragment.setOnTapArPlaneListener { hitResult: HitResult, _: Plane?, _: MotionEvent? ->
//                val anchor = hitResult.createAnchor()
//                if (anchorNode == null) {
//                    anchorNode = AnchorNode(anchor)
//                    anchorNode!!.setParent(arFragment.arSceneView.scene)
//                    createModel()
//                }
//            }
//        } catch (e: Exception) {
//            Log.e(TAG, "${e.printStackTrace()} ${e.message}")
//            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun createModel() {
//        try {
//            if (anchorNode != null) {
//                val node = TransformableNode(arFragment.transformationSystem)
//                node.scaleController.maxScale = 1f
//                node.scaleController.minScale = 0.1f
//                node.setParent(anchorNode)
//                node.renderable = objectRenderable
//
//                node.select()
//            }
//        } catch (e: Exception) {
//            Log.e(TAG, "createModel: ${e.printStackTrace()} ${e.message}")
//            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun loadModel() {
//        try {
//            ModelRenderable.builder().setSource(this, Uri.parse(modelUrl))
////            ModelRenderable.builder()
////                    .setSource(this,
////                            RenderableSource.builder().setSource(
////                                    this,
////                                    Uri.parse(modelUrl),
////                                    RenderableSource.SourceType.GLB
////                            )/*RenderableSource.SourceType.GLTF2)*/
////                                    .setScale(0.75f)
////                                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
////                                    .build()
////                    )
////                    .setRegistryId(modelUrl)
//                    .build()
//                    .thenAccept {
//                        objectRenderable = it
//                        snackbar.dismiss()
//                        snackbar = SnackbarHelper.getSnackbar(this, getString(R.string.model_ready), Snackbar.LENGTH_LONG)
////                        Toast.makeText(this, getString(R.string.model_ready), Toast.LENGTH_SHORT).show()
//snackbar.show()
//                    }
//                    .exceptionally {
//                        Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
//                        Log.i(TAG, "Can't load Model")
//                        null
//                    }
//        } catch (e: Exception) {
//            Log.e(TAG, "loadModel: ${e.message}")
//            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
//            e.printStackTrace()
//        }
//    }
//
//    companion object {
//        private const val TAG = "ARActivity"
//    }
//}
//
//
////class ARActivity : AppCompatActivity() {
////    private val MIN_OPENGL_VERSION = 3.0
////
////    private lateinit var mContentViewBinding: ActivityArBinding
////    private var arFragment: ArFragment? = null
////    private var objectRenderable: ModelRenderable? = null
////    private var mUserRequestedInstall = false
////    private var mSession: Session? = null
////
////    private var mModelStateSnackBar: Snackbar? = null
////    private lateinit var mModelCompletableFuture: CompletableFuture<Void>
////
////    private lateinit var arModel: String
////
////    override fun onNewIntent(intent: Intent?) {
////        super.onNewIntent(intent)
////        setIntent(intent)
////    }
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        arModel = intent.getStringExtra("ar_android").toString()
////
//////        arModel = arModel.substring(0, arModel.indexOf('?'))
//////        Log.i(TAG, "onCreate: $arModel")
////
////        if (checkIsSupportedDeviceOrFinish(this)) {
////            mContentViewBinding = DataBindingUtil.setContentView(this, com.webkul.mobikul.odoo.R.layout.activity_ar)
////            if (arModel.isNotEmpty() && arModel.isNotBlank())
////                startInitialization()
////        } else {
////            Log.i(TAG, "onCreate: not supported")
////            Toast.makeText(this, getString(R.string.the_ar_feature_is_not_supported_by_your_device), Toast.LENGTH_SHORT).show()
////            // MakeToast.instance.shortToast(this, getString(R.string.the_ar_feature_is_not_supported_by_your_device))
////             this.finish()
////        }
////    }
////
////    private fun startInitialization() {
////        Log.i(TAG, "startInitialization: ${arModel}")
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                createArSession()
////                arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
////                if (mSession != null) {
////                    val cfg = Config(mSession)
////                    cfg.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
////                    cfg.focusMode = Config.FocusMode.AUTO
////                    mSession!!.configure(cfg)
////                    arFragment!!.arSceneView.setupSession(mSession)
////                }
////
////                // Build texture sampler
//////                val sampler = Texture.Sampler.builder()
//////                        .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
//////                        .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
//////                        .setWrapMode(Texture.Sampler.WrapMode.REPEAT).build()
////
////                // Build texture with sampler
//////                val trigrid = Texture.builder()
//////                        .setSource(this, R.drawable.ar_background)
//////                        .setSampler(sampler).build()
////
////                // Set plane texture
//////                arFragment!!.arSceneView
//////                        .planeRenderer
//////                        .material
//////                        .thenAcceptBoth<Texture>(trigrid) { material, texture -> material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture) }
////
////                // Init renderable
////                loadModel()
////
////                // Model loading listener
////
////                // Set tap listener
////                arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, _: Plane, _: MotionEvent ->
////                    if (objectRenderable != null) {
////                        // Create the Anchor.
////                        val anchor = hitResult.createAnchor()
////                        val anchorNode = AnchorNode(anchor)
////                        anchorNode.setParent(arFragment!!.arSceneView.scene)
////
////                        // Create the transformable andy and add it to the anchor.
////                        val andy = TransformableNode(arFragment!!.transformationSystem)
////                        andy.setParent(anchorNode)
////                        andy.renderable = objectRenderable
////                        andy.select()
////                        mModelStateSnackBar?.dismiss()
////                    }
////                }
////            } else {
////                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
////                requestPermissions(permissions, 101)
////            }
////        }
////    }
////
////    @RequiresApi(api = Build.VERSION_CODES.N)
////    private fun loadModel() {
////        try {
////            Log.d(TAG, "=====>" + arModel)
////            mModelCompletableFuture = ModelRenderable.builder()
////                    .setSource(this, Uri.parse(arModel))
////                    .build()
////                    .thenAccept { renderable -> objectRenderable = renderable }
////                    .exceptionally { throwable ->
////                        Toast.makeText(this, getString(com.webkul.mobikul.odoo.R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
////                        throwable.printStackTrace()
////                        null
////                    }
//////
//////            mModelCompletableFuture = ModelRenderable.builder()
//////                    .setSource(this, R.raw.dinning_table)
//////                    .build()
//////                    .thenAccept { renderable -> objectRenderable = renderable }
//////                    .exceptionally { throwable ->
//////                        MakeToast.instance.shortToast(this,getString(R.string.something_went_wrong))
//////                        throwable.printStackTrace()
//////                        null
//////                    }
////
////            mModelStateSnackBar = Snackbar.make(mContentViewBinding.root, getString(com.webkul.mobikul.odoo.R.string.downloading_model), Snackbar.LENGTH_INDEFINITE)
////            mModelStateSnackBar!!.view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
////            mModelStateSnackBar!!.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(ContextCompat.getColor(this, com.webkul.mobikul.odoo.R.color.black))
////            mModelStateSnackBar?.show()
////
////            if (::mModelCompletableFuture.isInitialized)
////                checkIfModelReady()
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////    }
////
////    @RequiresApi(api = Build.VERSION_CODES.N)
////    private fun checkIfModelReady() {
////        if (mModelCompletableFuture.isDone && !isDestroyed) {
////            if (mModelCompletableFuture.isCompletedExceptionally || mModelCompletableFuture.isCancelled) {
////                mModelStateSnackBar = Snackbar.make(mContentViewBinding.root, getString(com.webkul.mobikul.odoo.R.string.something_went_wrong), Snackbar.LENGTH_INDEFINITE).setAction(getString(com.webkul.mobikul.odoo.R.string.retry)) {
////                    loadModel()
////                    mModelStateSnackBar?.dismiss()
////                }
////                mModelStateSnackBar!!.view.setBackgroundColor(ContextCompat.getColor(this, com.webkul.mobikul.odoo.R.color.colorPrimary))
////                mModelStateSnackBar!!.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(ContextCompat.getColor(this, com.webkul.mobikul.odoo.R.color.black))
////                mModelStateSnackBar?.show()
////            } else {
////                mModelStateSnackBar = Snackbar.make(mContentViewBinding.root, getString(com.webkul.mobikul.odoo.R.string.model_ready),
////                        Snackbar.LENGTH_INDEFINITE).setAction(getString(com.webkul.mobikul.odoo.R.string.cancel)) {
////                    mModelStateSnackBar?.dismiss()
////                    finish()
////                }
////                mModelStateSnackBar!!.view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
////                mModelStateSnackBar!!.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(ContextCompat.getColor(this, com.webkul.mobikul.odoo.R.color.black))
////                mModelStateSnackBar?.show()
////            }
////        } else {
////            Handler().postDelayed({ this.checkIfModelReady() }, 500)
////        }
////    }
////
////    @SuppressLint("ServiceCast")
////    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
////        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
////            Log.e("error", "Sceneform requires Android N or later")
////            return false
////        }
////        val openGlVersionString = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
////                .deviceConfigurationInfo
////                .glEsVersion
////        if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
////            Log.e("error", "Sceneform requires OpenGL ES 3.1 later")
////            return false
////        }
////        return true
////    }
////
////    private fun createArSession() {
////        var exception: Exception? = null
////        var message: String? = null
////        try {
////            if (mSession == null) {
////                when (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
////                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> mUserRequestedInstall = true
////                    ArCoreApk.InstallStatus.INSTALLED -> {
////                    }
////                }
////                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
////                    ActivityCompat.requestPermissions(
////                            this, arrayOf(Manifest.permission.CAMERA), 101)
////                    return
////                }
////                mSession = Session(this)
////            }
////        } catch (e: UnavailableArcoreNotInstalledException) {
////            message = getString(com.webkul.mobikul.odoo.R.string.ar_core_install_error)
////            exception = e
////        } catch (e: UnavailableUserDeclinedInstallationException) {
////            message = getString(com.webkul.mobikul.odoo.R.string.ar_core_install_error)
////            exception = e
////        } catch (e: UnavailableApkTooOldException) {
////            message = getString(com.webkul.mobikul.odoo.R.string.ar_core_update_error)
////            exception = e
////        } catch (e: UnavailableSdkTooOldException) {
////            message = getString(com.webkul.mobikul.odoo.R.string.ar_core_update_error)
////            exception = e
////        } catch (e: UnavailableDeviceNotCompatibleException) {
////            message = getString(com.webkul.mobikul.odoo.R.string.the_ar_feature_is_not_supported_by_your_device)
////            exception = e
////        } catch (e: Exception) {
////            message = getString(com.webkul.mobikul.odoo.R.string.ar_core_session_error)
////            exception = e
////        }
////
////        if (message != null) {
////            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
////            exception?.printStackTrace()
////        }
////    }
////
////    override fun onDestroy() {
////        super.onDestroy()
////        if (mModelCompletableFuture != null)
////            mModelCompletableFuture.cancel(true)
////    }
////
////    companion object {
////        private const val TAG = "ArActivity"
////    }
////}