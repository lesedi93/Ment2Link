package coment.github.academy_intern.ment2link.pojo;

import android.net.Uri;

/**
 * Created by team_leader on 2018/03/05.
 */

public class DownloadUrl {

    private Uri uri;
    private String uid;

    public DownloadUrl(Uri uri, String uid) {
        this.uri = uri;
        this.uid = uid;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}