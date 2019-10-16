package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nugu.uniseoul.R;
import com.nugu.uniseoul.data.ReviewData;

import java.util.List;

public class RivewRecyclerViewAdapter extends RecyclerView.Adapter<RivewRecyclerViewAdapter.MyViewHolder>{

    private List<ReviewData>  mDataset;
    private Context context;

    //줄의 요소
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title_textview;
        private TextView email_textview;
        private TextView content_textview;



        public MyViewHolder(View v) {
            super(v);
            title_textview = (TextView)v.findViewById(R.id.rowTitle);
            email_textview = (TextView)v.findViewById(R.id.rowEmail);
            content_textview = (TextView) v.findViewById(R.id.rowContent);
        }
    }

    //constructor
    public RivewRecyclerViewAdapter(List<ReviewData> myDataset, Context context) {
        mDataset = myDataset; //{"_id":"5d6f240ab8f831638ea91d60","id":"서울시립미술관","title":"test","content":"test","__v":0}
        this.context = context;
    }

    //1row
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_review, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ReviewData review = mDataset.get(position);


        holder.title_textview.setText(review.getTitle());
        holder.email_textview.setText(review.getEmail());
        holder.content_textview.setText(review.getContent());
        setReadMore(holder.content_textview,review.getContent(),3);


    }


    public static void setReadMore(final TextView view, final String text, final int maxLine) {
        final Context context = view.getContext();
        final String expanedText = " ... 더보기";

        if (view.getTag() != null && view.getTag().equals(text)) { //Tag로 전값 의 text를 비교하여똑같으면 실행하지 않음.
            return;
        }
        view.setTag(text); //Tag에 text 저장
        view.setText(text); // setText를 미리 하셔야  getLineCount()를 호출가능
        view.post(new Runnable() { //getLineCount()는 UI 백그라운드에서만 가져올수 있음
            @Override
            public void run() {
                if (view.getLineCount() >= maxLine) { //Line Count가 설정한 MaxLine의 값보다 크다면 처리시작

                    int lineEndIndex = view.getLayout().getLineVisibleEnd(maxLine - 1); //Max Line 까지의 text length

                    String[] split = text.split("\n"); //text를 자름
                    int splitLength = 0;

                    String lessText = "";
                    for (String item : split) {
                        splitLength += item.length() + 1;
                        if (splitLength >= lineEndIndex) { //마지막 줄일때!
                            if (item.length() >= expanedText.length()) {
                                lessText += item.substring(0, item.length() - (expanedText.length())) + expanedText;
                            } else {
                                lessText += item + expanedText;
                            }
                            break; //종료
                        }
                        lessText += item + "\n";
                    }
                    SpannableString spannableString = new SpannableString(lessText);
                    spannableString.setSpan(new ClickableSpan() {//클릭이벤트
                        @Override
                        public void onClick(View v) {
                            view.setText(text);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) { //컬러 처리
                            ds.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        }
                    }, spannableString.length() - expanedText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.setText(spannableString);
                    view.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
}
