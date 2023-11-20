package com.clova.anifriends.domain.common.event;

import com.clova.anifriends.domain.common.ImageRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ImageDeletionListener {

    private final ImageRemover imageRemover;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleImageDeletionEvent(ImageDeletionEvent imageDeletionEvent) {
        imageRemover.deleteImages(imageDeletionEvent.imageUrls());
    }

}
