package org.gaochun.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加一个点击事件
        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDexClass();
            }
        });

        mTextView = findViewById(R.id.text);
    }

    /**
     * 加载dex文件中的class，并调用其中的showToast方法
     */
    private void loadDexClass() {
        File dexOutputDir = getDir("dex", 0);//在data/data/xx包名/下面创建一个app_dex文件夹
        String internalPath = dexOutputDir.getAbsolutePath() + File.separator + "testDex_dex.jar";
        File dexFile = new File(internalPath);
        try {
            if (!dexFile.exists()) {
                dexFile.createNewFile();
                FileUtils.copyFiles(this, "testDex_dex.jar", dexFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //下面开始加载dex class
        //1.待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限,
        //2.解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写
        //3.指向包含本地库(so)的文件夹路径，可以设为null
        //4.父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。

        //注：4.1以后不能够将optimizedDirectory（即第二个参数）设置到sd卡目录， 否则抛出异常. 所以我们使用内部存储路径dexOutputDir
        // optimizedDirectory：解压后的.dex文件的存储路径，不能为空。这个路径强烈建议使用应用程序的私有路径，不要放到sdcard上，否则代码容易被注入攻击。
        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, dexOutputDir.getAbsolutePath(), null, getClassLoader());
        try {
            //该name就是internalPath路径下的dex文件里面的ShowToastImpl这个类的包名+类名
            Class<?> clz = dexClassLoader.loadClass("org.gaochun.dexlibrary1.ShowMessageImpl_one");
            IMessage impl = (IMessage) clz.newInstance();//通过该方法得到IMessage类
            if (impl != null) {
                String value = impl.showMessage(this);//调用打开弹窗并获取值
                mTextView.setText(value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
