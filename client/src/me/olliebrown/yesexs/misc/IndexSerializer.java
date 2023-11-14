package me.olliebrown.yesexs.misc;

import me.olliebrown.yesexs.dump.DumpIndex;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class IndexSerializer {

    private IndexSerializer(){

    }

    public static void write(List<me.olliebrown.yesexs.dump.DumpIndex> indices, Path to) throws IOException {
        try (XMLEncoder encoder = new XMLEncoder(Files.newOutputStream(to))) {
            encoder.writeObject(indices.size());
            for(me.olliebrown.yesexs.dump.DumpIndex idx : indices) {
                encoder.writeObject(idx.getAddress());
                encoder.writeObject(idx.getFilePos());
                encoder.writeObject(idx.getSize());
            }
        }
    }

    public static List<me.olliebrown.yesexs.dump.DumpIndex> read(Path from) throws IOException {
        List<me.olliebrown.yesexs.dump.DumpIndex> indices = new ArrayList<>();
        try(XMLDecoder decoder = new XMLDecoder(Files.newInputStream(from))) {
            int count = (Integer) decoder.readObject();
            for(int i = 0; i < count; i++) {
                long addr = (Long) decoder.readObject();
                long pos = (Long) decoder.readObject();
                long size = (Long) decoder.readObject();
                indices.add(new DumpIndex(addr, pos, size));
            }
        }
        return indices;
    }
}
