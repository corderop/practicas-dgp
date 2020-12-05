package com.example.vality;

import androidx.recyclerview.widget.RecyclerView;

public class ContenedorMensajeRecibido extends RecyclerView.Adapter {
    extView textoMensaje, textoHora, textoNombre;
    ImageView fotoPefil;

    ContenedorMensajeRecibido(View itemView) {
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
    }

    void bind(UserMessage message) {
        messageText.setText(message.getMessage());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        nameText.setText(message.getSender().getNickname());

        // Insert the profile image from the URL into the ImageView.
        Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
    }
}
