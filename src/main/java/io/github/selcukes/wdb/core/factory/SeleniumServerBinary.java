package io.github.selcukes.wdb.core.factory;

import io.github.selcukes.wdb.core.MirrorUrls;
import io.github.selcukes.wdb.enums.DownloaderType;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.exception.WebDriverBinaryException;
import io.github.selcukes.wdb.util.HttpUtils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class SeleniumServerBinary extends AbstractBinary {
    private static final String BINARY_DOWNLOAD_URL_PATTERN = "%s/%s/selenium-server-%s.jar";

    public SeleniumServerBinary(String release, TargetArch targetArch, String proxyUrl) {
        super(release, targetArch, proxyUrl);
    }

    @Override
    public Optional<URL> getDownloadURL() {
        try {
            return Optional.of(new URL(String.format(
                BINARY_DOWNLOAD_URL_PATTERN,
                MirrorUrls.SELENIUM_SERVER_URL,
                getBinaryVersion().contains("alpha") ? latestVersion : getBinaryVersion(),
                getBinaryVersion())));

        } catch (MalformedURLException e) {
            throw new WebDriverBinaryException(e);
        }
    }

    @Override
    public String getBinaryFileName() {
        return getBinaryDriverName() + ".jar";
    }

    @Override
    public DownloaderType getCompressedBinaryType() {
        return DownloaderType.JAR;
    }

    @Override
    public String getBinaryDriverName() {
        return "selenium-server";
    }

    @Override
    protected String getLatestRelease() {
        final InputStream downloadStream = HttpUtils.getResponseInputStream(MirrorUrls.SELENIUM_SERVER_LATEST_RELEASE_URL, null);
        return getVersionNumber(downloadStream,getBinaryDriverName());
    }

}