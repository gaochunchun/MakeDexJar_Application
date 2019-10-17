package org.gaochun.dexlibrary1;

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
     * 加载dex文件中的class，并调用其中的showMessage方法
     */
    private void loadDexClass() {
        File dexOutputDir = getDir("dex", 0);//在data/data/xx包名/下面创建一个app_dex文件夹
        String internalPath = dexOutputDir.getAbsolutePath() + File.separator + "dexlibrary1_dex.jar";
        File dexFile = new File(internalPath);
        try {
            if (!dexFile.exists()) {
                dexFile.createNewFile();
                //将assets目录下的文件copy到app/data/cache目录
                FileUtils.copyFiles(this, "dexlibrary1_dex.jar", dexFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //加载dex class
        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, dexOutputDir.getAbsolutePath(), null, getClassLoader());
        try {
            //该name就是internalPath路径下的dex文件里面的ShowMessageImpl_one这个类的包名+类名
            Class<?> clz = dexClassLoader.loadClass("org.gaochun.dexlibrary1.ShowMessageImpl_one");
            IMessage_one impl = (IMessage_one) clz.newInstance();//通过该方法得到IMessage_one类
            if (impl != null) {
                String value = impl.showMessage(this);//调用打开弹窗并获取值
                mTextView.setTextColor(getResources().getColor(R.color.red));
                mTextView.setText(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * DexClassLoader类参数含义
     * @param dexPath   待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限
     * @param optimizedDirectory    解压后的.dex文件存储路径，不可为空，此位置一定要是可读写且仅该应用可读写
     * @param librarySearchPath     指向包含本地库(so)的文件夹路径，可以设为null
     * @param parent    父级类加载器，一般可以通过Context.getClassLoader获取到，也可通过ClassLoader.getSystemClassLoader()获取到
     */
    //注：4.1以后不能够将第二个参数optimizedDirectory设置到sd卡目录， 否则抛出异常. 强烈建议使用内部私有存储路径（即应用的data/data/xx包名/下面创建一个app_dex文件夹），不要放到sdcard上，代码容易被注入攻击
    //public DexClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent)

}
