package dataModels;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by no-one on 15.01.17.
 */
public class File {
    @Setter @Getter private String name;
    @Setter @Getter private String type;
    @Setter @Getter private byte[] data;
}
