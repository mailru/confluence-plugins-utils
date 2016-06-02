package ru.mail.confluence.plugins.utils.spacevariables;

import com.atlassian.plugin.PluginAccessor;
import com.atlassian.spring.container.ContainerManager;
import ru.mail.confluence.plugins.utils.common.Consts;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageGeneratorServlet extends HttpServlet {
    private final SpaceVariableManager spaceVariableManager;

    public ImageGeneratorServlet(SpaceVariableManager spaceVariableManager) {
        this.spaceVariableManager = spaceVariableManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter(Consts.SPACE_VARIABLES_MACRO_ID_PARAMETER);

        if (id == null) {
            resp.setStatus(404);
            return;
        }

        PluginAccessor pa = (PluginAccessor) ContainerManager.getComponent("pluginAccessor");
        InputStream in = pa.getDynamicResourceAsStream("ru/mail/confluence/plugins/utils/spacevariables/space-variables-macro-placeholder.png");

        BufferedImage bufferedImage = ImageIO.read(in);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Font font = new Font("Arial", Font.PLAIN, 14);
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);

        String spaceVariableName = "#" + spaceVariableManager.getVariable(Integer.parseInt(id)).getName();
        int txtWidth = graphics.getFontMetrics().stringWidth(spaceVariableName);
        int txtHeight = graphics.getFontMetrics().getHeight();
        int imgWidth = bufferedImage.getWidth();
        int imgHeight = bufferedImage.getHeight();
        int xBuffer = 20;
        int eWidth = graphics.getFontMetrics().stringWidth("...");
        if ((txtWidth + xBuffer) > imgWidth) {
            String newText = "";
            int pos = 0;
            while (graphics.getFontMetrics().stringWidth(newText) + eWidth + xBuffer <= imgWidth) {
                newText += spaceVariableName.charAt(pos);
                ++pos;
            }
            spaceVariableName = newText + "...";
        }
        int yPos = (imgHeight / 2) + (txtHeight / 2) - graphics.getFontMetrics().getDescent();
        int xPos = (imgWidth / 2) - (graphics.getFontMetrics().stringWidth(spaceVariableName) / 2);

        graphics.drawString(spaceVariableName, xPos, yPos);
        resp.setContentType("image/png");
        ImageIO.write(bufferedImage, "png", resp.getOutputStream());
    }
}
