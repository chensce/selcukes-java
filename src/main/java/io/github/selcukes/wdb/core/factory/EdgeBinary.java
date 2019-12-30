package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.DriverType;
import io.github.selcukes.wdb.enums.OSType;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.util.HttpUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.jsoup.Jsoup.parse;

public class EdgeBinary extends AbstractBinary{
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/edgedriver_%s.zip";

    @Override
    public URL getDownloadURL() {
        try {
            return new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.EDGE_DRIVER_URL,
                getBinaryVersion(),
                getBinaryEnvironment().getOSType().equals(OSType.LINUX) ? "win" + getBinaryEnvironment().getArchitecture() : getBinaryEnvironment().getOsNameAndArch()
            ));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public String getBinaryDriverName() {
        return "msedgedriver";
    }

    @Override
    public DriverType getDriverType() {
        return DriverType.EDGE;
    }

    @Override
    protected String getLatestRelease() {
        List<String> versionNumbers = new ArrayList<>();
        String latestVersion;
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.EDGE_DRIVER_LATEST_RELEASE_URL, getProxy());
        try {
            Document doc = parse(downloadStream, null, "");

            Elements versionParagraph = doc.select(
                "ul.driver-downloads li.driver-download p.driver-download__meta");

            for (Element element : versionParagraph) {
                if (element.text().toLowerCase().startsWith("version")) {
                    String[] version = element.text().split(" ");
                    versionNumbers.add(version[1]);
                }
            }

            latestVersion = versionNumbers.get(0);

        } catch (Exception e) {
            throw new WebDriverBinaryException(e);
        }
        return latestVersion;
    }
}