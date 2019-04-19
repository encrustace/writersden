package in.encrust.writersden;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontHolder> {
    private Context context;
    private List<Typeface> fontList;
    private List<String> fontNameList;

    FontAdapter(Context context, List<Typeface> fontList, List<String> fontNameList) {
        this.context = context;
        this.fontList = fontList;
        this.fontNameList = fontNameList;
    }

    @NonNull
    @Override
    public FontHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fonts, viewGroup, false);
        return new FontHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontHolder fontHolder, final int i) {
        final Typeface font = fontList.get(i);
        final String name = fontNameList.get(i);

        fontHolder.fontView.setTypeface(font);
        fontHolder.fontView.setText(name);
        fontHolder.fontView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("intent").putExtra("position", i);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                if (context instanceof Home) {

                    ((Home) context).updateBroadCast(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    class FontHolder extends RecyclerView.ViewHolder {
        TextView fontView;

        FontHolder(@NonNull View itemView) {
            super(itemView);

            fontView = itemView.findViewById(R.id.fonts_fontview);
        }
    }
}
