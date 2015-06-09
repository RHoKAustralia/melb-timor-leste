package org.rhok.linguist.code;

/**
 * Created by lachlan on 7/05/2015.
 */
public class PersonWord {

    public int personid;
    public int itemid;
    public String word;
    public String audiofilename;

    public PersonWord(int _personid, int _itemid, String _word, String _audiofilename) {
        personid = _personid;
        itemid = _itemid;
        word = _word;
        audiofilename = _audiofilename;
    }

    public String getAsJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        addField(sb, "itemid", this.itemid, false, true);
        addField(sb, "word", this.word, true, true);
        addField(sb, "audiofilename", this.audiofilename, true, false);
        sb.append("}");
        return sb.toString();
    }

    private void addField(StringBuilder sb, String name, Object value, Boolean useQuotes, Boolean addComma) {

        if (value != null) {
            sb.append(name);
            sb.append(": ");
            if (useQuotes) {
                sb.append("'");
            }
            sb.append(value);
            if (useQuotes) {
                sb.append("'");
            }
            if (addComma) {
                sb.append(",");
            }
        }
    }

}
