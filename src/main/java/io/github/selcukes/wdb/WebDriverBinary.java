package io.github.selcukes.wdb;

import io.github.selcukes.wdb.core.factory.*;
import io.github.selcukes.wdb.enums.TargetArch;
import io.github.selcukes.wdb.util.TempFileUtil;
import io.github.selcukes.wdb.util.WebDriverBinaryUtil;

public class WebDriverBinary {
    private String downloadLocation = TempFileUtil.getTempDirectory();
    private boolean strictDownload = false;
    private BinaryFactory binaryFactory;

    public static synchronized Builder chromeDriver() {
        return new WebDriverBinary().new Builder(new ChromeBinary());
    }

    public static synchronized Builder firefoxDriver() {
        return new WebDriverBinary().new Builder(new GeckoBinary());
    }

    public static synchronized Builder ieDriver() {
        return new WebDriverBinary().new Builder(new IExplorerBinary());
    }

    public static synchronized Builder edgeDriver() {
        return new WebDriverBinary().new Builder(new EdgeBinary());
    }

    public static synchronized Builder operaDriver() {
        return new WebDriverBinary().new Builder(new OperaBinary());
    }

    public static synchronized Builder grid() {
        return new WebDriverBinary().new Builder(new SeleniumServerBinary());
    }

    public class Builder {
        public Builder(BinaryFactory binaryFactory) {
            WebDriverBinary.this.binaryFactory = binaryFactory;
        }


        public Builder version(String version) {
            binaryFactory.setVersion(version);
            return this;
        }

        public Builder arch64() {
            binaryFactory.setTargetArch(TargetArch.X64);
            return this;
        }

        public Builder arch32() {
            binaryFactory.setTargetArch(TargetArch.X32);
            return this;
        }


        public Builder targetPath(String targetPath) {
            WebDriverBinary.this.downloadLocation = targetPath;
            return this;
        }

        public Builder proxy(String proxy) {
            binaryFactory.setProxy(proxy);
            return this;
        }

        public Builder strictDownload() {
            WebDriverBinary.this.strictDownload = true;
            return this;
        }

        public BinaryInfo setup() {
            return new WebDriverBinaryUtil(WebDriverBinary.this.binaryFactory,
                WebDriverBinary.this.downloadLocation,
                WebDriverBinary.this.strictDownload).downloadAndSetupBinaryPath();
        }
    }

}
