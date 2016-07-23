package ru.yamblz.translatetraining;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartMenuAdapter extends RecyclerView.Adapter<StartMenuAdapter.StartMenuItemViewHolder> {

    int[] titles = {
            R.string.start_menu_titles_cards,
            R.string.start_menu_titles_saying,
            R.string.start_menu_titles_hear,
            R.string.start_menu_titles_pairing,
            R.string.start_menu_titles_compose,
            R.string.start_menu_titles_true_false,
            R.string.start_menu_titles_answer,
            R.string.start_menu_titles_images
    };
    int[] teasers = {
            R.drawable.cards_teaser,
            R.drawable.saying_teaser,
            R.drawable.hear_teaser,
            R.drawable.pairing_teaser,
            R.drawable.compose_teaser,
            R.drawable.true_false_teaser,
            R.drawable.answer_teaser,
            R.drawable.images_teaser
    };
    int[] descriptions = {
            R.string.start_menu_descriptions_cards,
            R.string.start_menu_descriptions_saying,
            R.string.start_menu_descriptions_hear,
            R.string.start_menu_descriptions_pairing,
            R.string.start_menu_descriptions_compose,
            R.string.start_menu_descriptions_true_false,
            R.string.start_menu_descriptions_answer,
            R.string.start_menu_descriptions_images
    };
    int[] colors = {
            0xffff5252,
            0xff4caf50,
            0xff9c27b0,
            0xff4fb0ae,
            0xffff5722,
            0xff2196f3,
            0xffff5858,
            0xff4caf50
    };
    int[] colors2 = {
            0xd32f2f,
            0x388e3c,
            0x7b1fa2,
            0x0097a7,
            0xe64a19,
            0x1976d2,
            0xd32f2f,
            0x388e3c
    };
    boolean[] isOpened = {false,false,false,false,false,false,false,false};

    private OnTitleClickListener onTitleClickListener;
    private OnStartClickListener onStartClickListener;

    private StartMenuItemViewHolder lastClickedItem;

    public StartMenuAdapter(OnTitleClickListener onTitleClickListener,
                            OnStartClickListener onStartClickListener) {
        this.onTitleClickListener = onTitleClickListener;
        this.onStartClickListener = onStartClickListener;
    }

    @Override
    public StartMenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_start_menu, parent, false);
        return new StartMenuItemViewHolder(view, onTitleClickListener, onStartClickListener);
    }

    @Override
    public void onBindViewHolder(StartMenuItemViewHolder holder, int position) {
        holder.tvTitle.setText(titles[position]);
        holder.ivTeaser.setImageResource(teasers[position]);
        holder.tvDescription.setText(descriptions[position]);
        holder.expandableLayout.setBackgroundColor(colors[position]);
        holder.tvStart.setBackgroundColor(colors2[position]);

        int width = holder.container.getWidth();

        if (isOpened[position])
            holder.container.findViewById(R.id.expandable_layout)
                    .setLayoutParams(new LinearLayout.LayoutParams(width, holder.container.getMeasuredHeight()));
        else
            holder.container.findViewById(R.id.expandable_layout).setLayoutParams(new LinearLayout.LayoutParams(width, 0));
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class StartMenuItemViewHolder extends RecyclerView.ViewHolder {

        View container;

        TextView tvTitle;
        ImageView ivTeaser;
        TextView tvDescription;
        TextView tvStart;
        LinearLayout expandableLayout;

        public StartMenuItemViewHolder(View itemView,
                                       final OnTitleClickListener titleClickListener,
                                       final OnStartClickListener startClickListener) {
            super(itemView);

            container = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivTeaser = (ImageView) itemView.findViewById(R.id.iv_teaser);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tvStart = (TextView) itemView.findViewById(R.id.tv_start);
            expandableLayout = (LinearLayout) itemView.findViewById(R.id.expandable_layout);

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOpened[getAdapterPosition()]) {
                        collapse(expandableLayout, tvTitle, 0xfffafafa);
                    } else {
                        tvTitle.setBackgroundColor(0xffff5252);
                        tvTitle.setTextColor(0xffffffff);
                        expand(expandableLayout);
                    }
                    isOpened[getAdapterPosition()] = !isOpened[getAdapterPosition()];
                }
            });
            tvStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startClickListener.onStartClickListener(getAdapterPosition());
                }
            });
        }

    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final int targetHeight = v.getMeasuredHeight();
        final int width = ((View) v.getParent()).getWidth();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }

        };

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setLayoutParams(new LinearLayout.LayoutParams(width, targetHeight));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v, final TextView textView, final int backgroundColor) {
        final int initialHeight = v.getMeasuredHeight();
        final int width = ((View) v.getParent()).getWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setBackgroundColor(backgroundColor);
                textView.setTextColor(0xff222222);
                v.setLayoutParams(new LinearLayout.LayoutParams(width, 0));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}

