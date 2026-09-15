package com.example.filippinotvapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filippinotvapp.models.Channel;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {
    
    private final List<Channel> channels;
    private final ChannelClickListener listener;
    private final Context context;
    private int selectedPosition = 0;
    
    public interface ChannelClickListener {
        void onChannelClick(Channel channel);
    }
    
    public ChannelAdapter(Context context, List<Channel> channels, ChannelClickListener listener) {
        this.context = context;
        this.channels = channels;
        this.listener = listener;
    }
    
    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_item, parent, false);
        return new ChannelViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Channel channel = channels.get(position);
        holder.bind(channel, position == selectedPosition);
    }
    
    @Override
    public int getItemCount() {
        return channels.size();
    }
    
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }
    
    public class ChannelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        private final TextView channelName;
        private final ImageView channelLogo;
        private final View selectedIndicator;
        private Channel channel;
        
        public ChannelViewHolder(View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.channel_name);
            channelLogo = itemView.findViewById(R.id.channel_logo);
            selectedIndicator = itemView.findViewById(R.id.selected_indicator);
            itemView.setOnClickListener(this);
        }
        
        public void bind(Channel channel, boolean isSelected) {
            this.channel = channel;
            channelName.setText(channel.getName());
            
            // Load logo image (you can use Picasso or Glide here)
            // For now, just show placeholder
            channelLogo.setImageResource(R.drawable.ic_channel_placeholder);
            
            // Show/hide selection indicator
            selectedIndicator.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            itemView.setActivated(isSelected);
        }
        
        @Override
        public void onClick(View v) {
            setSelectedPosition(getBindingAdapterPosition());
            if (listener != null && channel != null) {
                listener.onChannelClick(channel);
            }
        }
    }
}
