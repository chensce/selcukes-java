package io.github.selcukes.snapshot;

import io.github.selcukes.commons.Await;
import io.github.selcukes.commons.exception.SnapshotException;
import io.github.selcukes.commons.helper.ImageUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

class DefaultPageSnapshot {
    private WebDriver driver;
    protected String screenshotText;
    protected boolean isAddressBar = false;

    public DefaultPageSnapshot(WebDriver driver) {
        this.driver = driver;
    }

    protected <X> X getDefaultPageSnapshot(OutputType<X> outputType) {
        return outputType.convertFromPngBytes(ImageUtil.toByteArray(defaultPageScreenshot()));
    }

    protected Object executeJS(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    protected String getFullHeight() {
        return executeJS("return document.body.scrollHeight").toString();
    }

    protected int getFullWidth() {
        Long fullWidth = (Long) executeJS("return window.innerWidth");
        return fullWidth.intValue();
    }

    protected int getWindowHeight() {
        Long windowHeight = (Long) executeJS("return window.innerHeight");
        return windowHeight.intValue();
    }

    protected void waitForScrolling() {
        Await.until(TimeUnit.MILLISECONDS, 1);
    }

    public byte[] shootPageAsBytes() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getScreenshotText() {
        String pageUrl = "Current Page Url: " + getCurrentUrl();
        if (screenshotText == null)
            return pageUrl;
        return pageUrl + "\n" + screenshotText;
    }

    private BufferedImage defaultPageScreenshot() {
        int allH = Integer.parseInt(getFullHeight());
        int allW = getFullWidth();
        int winH = getWindowHeight();
        int scrollTimes = allH / winH;
        int tail = allH - winH * scrollTimes;
        BufferedImage finalImage = new BufferedImage(allW, allH, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalImage.createGraphics();
        for (int n = 0; n < scrollTimes; n++) {
            executeJS("scrollTo(0, arguments[0])", winH * n);
            waitForScrolling();
            BufferedImage part = ImageUtil.toBufferedImage(shootPageAsBytes());
            graphics.drawImage(part, 0, n * winH, null);
        }
        if (tail > 0) {
            executeJS("scrollTo(0, document.body.scrollHeight)");
            waitForScrolling();
            BufferedImage last = ImageUtil.toBufferedImage(shootPageAsBytes());
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * winH, null);
        }
        graphics.dispose();
        return finalImage;
    }

    public BufferedImage captureAddressBar(int width) {
        Rectangle screenRectangle = new Rectangle(0, 0, width, 70);
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(screenRectangle);
        } catch (AWTException e) {
            throw new SnapshotException("Failed capturing Browser address bar...", e);
        }
    }
}
