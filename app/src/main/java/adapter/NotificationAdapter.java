package adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zaykazip.databinding.NotificationItemBinding;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final ArrayList<String> notifications;
    private final ArrayList<Integer> notificationImages;

    public NotificationAdapter(ArrayList<String> notifications, ArrayList<Integer> notificationImages) {
        this.notifications = notifications;
        this.notificationImages = notificationImages;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemBinding binding = NotificationItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final NotificationItemBinding binding;

        public NotificationViewHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(int position) {
            binding.notificationTextView.setText(notifications.get(position));
            binding.notificationImageView.setImageResource(notificationImages.get(position));
        }
    }
}
