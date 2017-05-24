package hackerman.notebookmushroom.common;

import com.hannesdorfmann.mosby.mvp.MvpActivity;



public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>> extends MvpActivity<V, P> {
}
