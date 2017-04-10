package no.fjordkraft.afi.server.emuxml.services.impl;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuXmlHandleRequest;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

@Slf4j
public class ParseEmuXmlServiceImplTest {

    ParseEmuXmlServiceImpl classUnderTest;

    @Before
    public void before() {
        classUnderTest = new ParseEmuXmlServiceImpl(new AttachmentStoreServiceImpl(), new IF318StoreServiceImpl());
    }

    @Test
    public void testParseFile_1() {

        try {
            File file = new File(getClass().getClassLoader().getResource("emu/ALBU06_153_50153687_EK_04.emu16.xml").getFile());
            String path = file.getAbsoluteFile().getParent();

            FileUtils.listFiles(file.getAbsoluteFile().getParentFile(), null, false).stream()
                    .filter(f -> f.getName().startsWith("ALBU") && f.getName().endsWith(".emu16.xml"))
                    .forEach(f -> {
                        Stopwatch sw = Stopwatch.createStarted();

                        classUnderTest.parseFile(EmuXmlHandleRequest.parseAndStore(f.getName(), false, false),
                                path,
                                path + File.separator + ".." + File.separator + f.getName(),
                                path + File.separator + ".." + File.separator + f.getName()
                        );

                        System.out.println(String.format("parsed EMU XML: %s  (%s)", f.getName(), sw.stop().toString()));
                    });
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }
}
