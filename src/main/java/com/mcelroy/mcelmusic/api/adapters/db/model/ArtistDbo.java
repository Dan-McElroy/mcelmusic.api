package com.mcelroy.mcelmusic.api.adapters.db.model;

import com.mcelroy.mcelmusic.api.domain.model.Artist;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;

import java.sql.Timestamp;

@Value
@Builder(toBuilder = true)
@TypeAlias("Artist")
public class ArtistDbo implements Persistable<String> {

    @Id
    String id;

    @Version
    int version;

    @CreatedDate
    Timestamp creationTime;

    public static ArtistDbo fromArtist(Artist artist) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Artist toArtist(ArtistDbo artist) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }
}
