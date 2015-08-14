package com.jin91.preciousmetal.ui.service.play;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.customview.emo.EmoGridView;
import com.jin91.preciousmetal.customview.emo.Emoparser;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.jin91.preciousmetal.util.InputUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/15.
 */
@SuppressLint("NewApi")
public abstract class BasePlayCommFragment<T> extends BasePlayFragment<T> {


    @ViewInject(R.id.iv_text_emo)
    ImageView ivTextEmo;
    @ViewInject(R.id.et_comment_content)
    EditText etCommentContent;
    @ViewInject(R.id.emoGrideView)
    EmoGridView emoGrideView;
    private boolean textChanged;

    protected LoadingDialog loadingDialog;

    protected String replyType; // 回复的类型 // 2--回复或者交流 1 提问
    protected String replyToId; // 回复的id  如果是交流传0,回复传回复的id,提问的时候不传
    public void setCommentListener() {
        ivTextEmo.setTag("emo");
        BuildApiUtil.setCursorDraw(etCommentContent);
        emoGrideView.setOnEmoGridViewItemClick(new EmoGridView.OnEmoGridViewItemClick() {
            @Override
            public void onItemClick(int facesPos, int viewIndex) {

                textChanged = false;
                int deleteId = (++viewIndex) * (Constants.pageSize - 1);
                if (deleteId > Emoparser.getInstance(mContext.getApplicationContext()).getResIdList().length) {
                    deleteId = Emoparser.getInstance(mContext.getApplicationContext()).getResIdList().length;
                }
                if (deleteId == facesPos) {
                    String msgContent = etCommentContent.getText().toString();
                    if (TextUtils.isEmpty(msgContent)) {
                        return;
                    }
                    if (msgContent.contains("[")) {
                        msgContent = msgContent.substring(0, msgContent.lastIndexOf("[EMOT]"));
                    }
                    etCommentContent.setText(msgContent);
                } else {
                    int resId = Emoparser.getInstance(mContext.getApplicationContext()).getResIdList()[facesPos];
                    String pharse = Emoparser.getInstance(mContext.getApplicationContext()).getIdPhraseMap().get(resId);
                    int startIndex = etCommentContent.getSelectionStart();
                    Editable edit = etCommentContent.getEditableText();
                    if (startIndex < 0 || startIndex >= edit.length()) {
                        if (null != pharse) {
                            edit.append(pharse);
                        }
                    } else {
                        if (null != pharse) {
                            edit.insert(startIndex, pharse);
                        }
                    }
                }
                Editable edtable = etCommentContent.getText();
                int position = edtable.length();
                Selection.setSelection(edtable, position);

            }
        });
        emoGrideView.setAdapter();
        etCommentContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.i(ServiceDetailActivity.TAG, "onTextChanged");
                if (s.length() > 0) {
                    String strMsg = etCommentContent.getText().toString();
                    CharSequence emoCharSeq = Emoparser.getInstance(mContext.getApplicationContext()).emoCharsequence(strMsg);
                    if (!textChanged) {
                        textChanged = true;
                        etCommentContent.setText(emoCharSeq);
                        Editable edtable = etCommentContent.getText();
                        int position = edtable.length();
                        Selection.setSelection(edtable, position);

                    } else {
                        textChanged = false;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Logger.i(ServiceDetailActivity.TAG, "beforeTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.i(ServiceDetailActivity.TAG, "afterTextChanged");
                textChanged = true;
            }
        });
        etCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivTextEmo.setImageResource(R.drawable.btn_emo_selector);
            }
        });
    }

    @OnClick({R.id.btn_send, R.id.iv_text_emo})
    public void onOtherClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send: // 发送
                String content = etCommentContent.getText().toString().trim();
                try {
                    presenter.sendComment(ServiceDetailActivity.TAG, replyType, replyToId, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_text_emo: // 表情或者输入框
                if ("emo".equals(ivTextEmo.getTag())) {
                    // 关闭输入法，显示表情
                    ivTextEmo.setTag("keyboard");
                    InputUtil.hideSoftInput(mContext, etCommentContent.getWindowToken());
                    emoGrideView.setVisibility(View.VISIBLE);
                    ivTextEmo.setImageResource(R.drawable.btn_keyboard_selector);
                } else {
                    emoGrideView.setVisibility(View.GONE);
                    ivTextEmo.setTag("emo");
                    ivTextEmo.setImageResource(R.drawable.btn_emo_selector);
                    InputUtil.openKeyboard(mContext, etCommentContent);
                }
                break;
        }
    }
    @Override
    public void hideInputMethod() {
        InputUtil.hideSoftInput(mContext, etCommentContent.getWindowToken());
    }

    @Override
    public void showToastMessage(String message) {
        MessageToast.showToast(message, 0);
    }

    @Override
    public void showProcessDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
        loadingDialog.show();
    }

    @Override
    public void hideProcessDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void sendSuccess() {
        MessageToast.showToast("发送成功", 0);
        etCommentContent.setText("");
    }
}
