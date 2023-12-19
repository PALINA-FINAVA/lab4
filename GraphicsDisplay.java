package bsu.rfe.group7.Finova.B7;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double scale;
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private Font axisFont;
    private boolean showModifiedCondition = true;

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[] {32.0f, 8.0f, 32.0f,  8.0f, 32.0f,  8.0f,  8.0f,  8.0f,  8.0f,  8.0f,  8.0f, 8.0f}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight() / scale - (maxY -
                    minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {
            double xIncrement = (getSize().getWidth() / scale - (maxX -
                    minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.BLACK);
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }
    /*private void drawCustomMarker(Graphics2D canvas, Point2D.Double center) {
        GeneralPath path = new GeneralPath();

        Double[] temp={0.0, 0.0};
        Point2D.Double tempPoint = xyToPoint(temp[0], temp[1]);
        path.moveTo(tempPoint.getX(), tempPoint.getY());

        Double[] temp1={11.0, 0.0};
        Point2D.Double tempPoint1 = xyToPoint(temp1[0], temp1[1]);
        path.moveTo(tempPoint1.getX(), tempPoint1.getY());

        Double[] temp2={11.0, 11.0};
        Point2D.Double tempPoint2 = xyToPoint(temp2[0], temp2[1]);
        path.moveTo(tempPoint2.getX(), tempPoint2.getY());

        Double[] temp3={0.0, 11.0};
        Point2D.Double tempPoint3 = xyToPoint(temp3[0], temp3[1]);
        path.moveTo(tempPoint3.getX(), tempPoint3.getY());

        Double[] temp4={11.0, 0.0};
        Point2D.Double tempPoint4 = xyToPoint(temp4[0], temp4[1]);
        path.moveTo(tempPoint4.getX(), tempPoint4.getY());

        Double[] temp5={11.0, 0.0};
        Point2D.Double tempPoint5 = xyToPoint(temp5[0], temp5[1]);
        path.moveTo(tempPoint5.getX(), tempPoint5.getY());

        Double[] temp6={0.0, 0.0};
        Point2D.Double tempPoint6 = xyToPoint(temp6[0], temp6[1]);
        path.moveTo(tempPoint6.getX(), tempPoint6.getY());

        Double[] temp7={11.0, 11.0};
        Point2D.Double tempPoint7 = xyToPoint(temp7[0], temp7[1]);
        path.moveTo(tempPoint7.getX(), tempPoint7.getY());

        path.closePath();
        canvas.draw(path);
    }*/
    private Point2D.Double constructPoint(
            Point2D.Double point,
            double dx,
            double dy) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(
                point.getX() + dx,
                point.getY() + dy
        );
        return dest;
    }


    protected boolean condition(Double[] point){
        String digits = Double.toString(point[1]);
        digits = digits.replace(".", "");
        digits = digits.replace("-", "");
        for (int i = 0; i < digits.length() - 1 ; i++) {
            if (Integer.parseInt(Character.toString(digits.charAt(i))) > Integer.parseInt(Character.toString(digits.charAt(i+1)))) {
                return false;
            }
        }
        return true;
    }
    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.BLACK);
        for (Double[] point : graphicsData) {
            if (condition(point))
                canvas.setColor(Color.RED);
            else
                canvas.setPaint(Color.BLACK);
           Point2D.Double center = xyToPoint(point[0], point[1]);

            Line2D.Double firstLine = new Line2D.Double();
            firstLine.setLine(

                    constructPoint(xyToPoint(point[0], point[1]), 0.0, 0.0),
                    constructPoint(xyToPoint(point[0], point[1]), 11.0, 0.0)
            );

            Line2D.Double secondLine = new Line2D.Double();
            secondLine.setLine(
                    constructPoint(xyToPoint(point[0], point[1]), 11.0, 0.0),
                    constructPoint(xyToPoint(point[0], point[1]), 11.0, 11.0)
            );

            Line2D.Double thirdLine = new Line2D.Double();
            thirdLine.setLine(

                    constructPoint(xyToPoint(point[0], point[1]), 11.0, 11.0),
                    constructPoint(xyToPoint(point[0], point[1]), 0.0, 11.0)
            );
            Line2D.Double forthLine = new Line2D.Double();
            forthLine.setLine(

                    constructPoint(xyToPoint(point[0], point[1]), 0.0, 11.0),
                    constructPoint(xyToPoint(point[0], point[1]), 0.0, 0.0)
            );
            Line2D.Double fifthLine = new Line2D.Double();
            fifthLine.setLine(

                    constructPoint(xyToPoint(point[0], point[1]), 0.0, 0.0),
                    constructPoint(xyToPoint(point[0], point[1]), 11.0, 11.0)
            );
            Line2D.Double sixthLine = new Line2D.Double();
            sixthLine.setLine(

                    constructPoint(xyToPoint(point[0], point[1]), 0.0, 11.0),
                    constructPoint(xyToPoint(point[0], point[1]), 11.0, 0.0)
            );
            canvas.draw(firstLine);
            canvas.draw(secondLine);
            canvas.draw(thirdLine);
            canvas.draw(forthLine);
            canvas.draw(fifthLine);
            canvas.draw(sixthLine);

            /*  drawCustomMarker(canvas, center);*/
        }
    }

    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
// Определить, должна ли быть видна ось Y на графике
        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
                    xyToPoint(0, minY)));
// Стрелка оси Y
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5,
                    arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10,
                    arrow.getCurrentPoint().getY());
// Замкнуть треугольник стрелки
            arrow.closePath();
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку
// Нарисовать подпись к оси Y
// Определить, сколько места понадобится для надписи "y"
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
// Вывести надпись в точке с вычисленными координатами
            canvas.drawString("y", (float) labelPos.getX() + 10,
                    (float) (labelPos.getY() - bounds.getY()));
        }
// Определить, должна ли быть видна ось X на графике
        if (minY <= 0.0 && maxY >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0),
                    xyToPoint(maxX, 0)));
// Стрелка оси X
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20,
                    arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(),
                    arrow.getCurrentPoint().getY() + 10);
// Замкнуть треугольник стрелки
            arrow.closePath();
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку
// Нарисовать подпись к оси X
// Определить, сколько места понадобится для надписи "x"
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
// Вывести надпись в точке с вычисленными координатами
            canvas.drawString("x", (float) (labelPos.getX() -
                    bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));
        }
       /* if (minX == 0.0 && minY == 0.0) {*/
            Rectangle2D bounds = axisFont.getStringBounds("0", context);
            Point2D.Double labelPos = xyToPoint(0.0, 0.0);
            canvas.drawString("0", (float) (labelPos.getX() -
                    bounds.getWidth() - 10), (float) (labelPos.getY() - bounds.getY()));
       /* }*/
    }

    /* Метод-помощник, осуществляющий преобразование координат.
    * Оно необходимо, т.к. верхнему левому углу холста с координатами
    * (0.0, 0.0) соответствует точка графика с координатами (minX, maxY),
    где
    * minX - это самое "левое" значение X, а
    * maxY - самое "верхнее" значение Y.
    */
    protected Point2D.Double xyToPoint(double x, double y) {
// Вычисляем смещение X от самой левой точки (minX)
        double deltaX = x - minX;
// Вычисляем смещение Y от точки верхней точки (maxY)
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    /* Метод-помощник, возвращающий экземпляр класса Point2D.Double
     * смещѐнный по отношению к исходному на deltaX, deltaY
     * К сожалению, стандартного метода, выполняющего такую задачу, нет.
     */
    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX,
                                        double deltaY) {
// Инициализировать новый экземпляр точки
        Point2D.Double dest = new Point2D.Double();

        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    public Double[][] getGraphicsData(){
        return graphicsData;
    }
}