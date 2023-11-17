package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Track;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Entity
@Builder(toBuilder = true)
@TypeAlias("Track")
public class TrackDbo {

    @Id
    String id;

    public static TrackDbo fromTrack(Track track) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Track toTrack(TrackDbo trackDbo) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
