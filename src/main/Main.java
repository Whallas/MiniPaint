package main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by whallas on 20/03/17.
 */
public class Main {

    private static final int WINDOW_WIDTH = 640;  // width of the drawable
    private static final int WINDOW_HEIGHT = 640; // height of the drawable
    private MyMouseAdapterSingleton myMouseAdapterSingleton;

    private Main() {
        //getting the capabilities object of GL2 profile
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas glCanvas = new GLCanvas(capabilities);
        MyGLEventListenerSingleton myGLEventListenerSingleton = MyGLEventListenerSingleton.getInstance();

        glCanvas.addGLEventListener(myGLEventListenerSingleton);
        glCanvas.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        JPanel jpOptions = new JPanel();
        jpOptions.setVisible(false);

        myMouseAdapterSingleton = MyMouseAdapterSingleton.getInstance(glCanvas, myGLEventListenerSingleton, jpOptions);
        glCanvas.addMouseListener(myMouseAdapterSingleton);
        glCanvas.addMouseMotionListener(myMouseAdapterSingleton);
        //creating frame
        final JFrame frame = new JFrame("Mini Paint");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(glCanvas, BorderLayout.LINE_START);

        JPanel settings = new JPanel();
        settings.setPreferredSize(new Dimension(180, WINDOW_HEIGHT));
        JPanel jpEscala = new JPanel();
        jpEscala.setVisible(false);
        JPanel jpPolygonSides = new JPanel();
        jpPolygonSides.setVisible(false);
        JPanel jpCisalhamento = new JPanel();
        jpCisalhamento.setVisible(false);

        JLabel jlSides = new JLabel("Número de lados:");
        JLabel jlEscala = new JLabel("Escala:");
        JLabel jlEX = new JLabel("X:");
        JLabel jlEY = new JLabel("Y:");
        JLabel jlCAxis = new JLabel("Eixo:");
        JLabel jlC = new JLabel("Valor de K:");
        jlEX.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14)); // NOI18N
        jlEY.setFont(new java.awt.Font("Dialog", Font.PLAIN, 14)); // NOI18N

        JTextField tfEX = new JTextField();
        JTextField tfEY = new JTextField();
        JTextField tfSides = new JTextField("5");
        JTextField tfC = new JTextField("0.0");

        ButtonGroup bgC = new ButtonGroup();
        JRadioButton rbCY = new JRadioButton("Y");
        JRadioButton rbCX = new JRadioButton("X");
        bgC.add(rbCY);
        bgC.add(rbCX);

        JButton btEscala = new JButton("Escalar");
        JButton btFechoConvexo = new JButton("Executar");

        JComboBox<String> cbActions = new JComboBox<>(new String[]{"", "Rotacionar", "Transladar", "Escalar", "Cisalhar", "Espelhar", "Fecho Convexo"});
        cbActions.setVisible(false);
        JComboBox<String> cbShapes = new JComboBox<>(new String[]{"", "Ponto", "Linha", "Polígono", "Elipse"});

        javax.swing.GroupLayout jpEscalaLayout = new javax.swing.GroupLayout(jpEscala);
        jpEscala.setLayout(jpEscalaLayout);
        jpEscalaLayout.setHorizontalGroup(
                jpEscalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpEscalaLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jpEscalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jpEscalaLayout.createSequentialGroup()
                                                .addComponent(jlEX)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfEX, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jlEY)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(tfEY, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(btEscala)
                                        .addComponent(jlEscala))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpEscalaLayout.setVerticalGroup(
                jpEscalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpEscalaLayout.createSequentialGroup()
                                .addComponent(jlEscala)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpEscalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlEX)
                                        .addComponent(tfEX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jlEY)
                                        .addComponent(tfEY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btEscala))
        );
        javax.swing.GroupLayout jpPolygonSidesLayout = new javax.swing.GroupLayout(jpPolygonSides);
        jpPolygonSides.setLayout(jpPolygonSidesLayout);
        jpPolygonSidesLayout.setHorizontalGroup(
                jpPolygonSidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpPolygonSidesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jlSides)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfSides, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jpPolygonSidesLayout.setVerticalGroup(
                jpPolygonSidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpPolygonSidesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jpPolygonSidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlSides)
                                        .addComponent(tfSides, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        javax.swing.GroupLayout jpCisalhamentoLayout = new javax.swing.GroupLayout(jpCisalhamento);
        jpCisalhamento.setLayout(jpCisalhamentoLayout);
        jpCisalhamentoLayout.setHorizontalGroup(
                jpCisalhamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpCisalhamentoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jpCisalhamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jpCisalhamentoLayout.createSequentialGroup()
                                                .addComponent(jlCAxis)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(rbCX))
                                        .addGroup(jpCisalhamentoLayout.createSequentialGroup()
                                                .addComponent(jlC)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(tfC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbCY)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpCisalhamentoLayout.setVerticalGroup(
                jpCisalhamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpCisalhamentoLayout.createSequentialGroup()
                                .addGroup(jpCisalhamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(rbCY)
                                        .addComponent(rbCX)
                                        .addComponent(jlCAxis, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpCisalhamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlC)
                                        .addComponent(tfC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btFechoConvexo.setText("Executar");

        javax.swing.GroupLayout jpOptionsLayout = new javax.swing.GroupLayout(jpOptions);
        jpOptions.setLayout(jpOptionsLayout);
        jpOptionsLayout.setHorizontalGroup(
                jpOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpOptionsLayout.createSequentialGroup()
                                .addGroup(jpOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jpEscala, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jpPolygonSides, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jpCisalhamento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jpOptionsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jpOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cbActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btFechoConvexo))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpOptionsLayout.setVerticalGroup(
                jpOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jpOptionsLayout.createSequentialGroup()
                                .addComponent(jpPolygonSides, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpCisalhamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpEscala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btFechoConvexo)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(settings);
        settings.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jpOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cbShapes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbShapes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        cbActions.addActionListener(e -> {
            int item = cbActions.getSelectedIndex();

            if (item != 3) jpEscala.setVisible(false);
            else jpEscala.setVisible(true);

            if (item != 4) jpCisalhamento.setVisible(false);
            else jpCisalhamento.setVisible(true);

            if (item != 6) btFechoConvexo.setVisible(false);
            else btFechoConvexo.setVisible(true);

            myMouseAdapterSingleton.setShapeAction(item);
        });

        cbShapes.addActionListener(e -> {
            int item = cbShapes.getSelectedIndex();

            jpOptions.setVisible(false);

            if (item != 3) jpPolygonSides.setVisible(false);
            else jpPolygonSides.setVisible(true);

            if (item != 0) {
                cbActions.setVisible(true);
                cbActions.setSelectedIndex(0);
            } else cbActions.setVisible(false);

            myMouseAdapterSingleton.changeCurrentShape(item);
        });

        tfSides.addActionListener((ActionEvent e) -> {
            try {
                int numSides = Integer.parseInt(tfSides.getText());
                if (numSides > 2) {
                    myMouseAdapterSingleton.setPolygonNumSides(numSides);
                    cbActions.setSelectedIndex(0);
                }
            } catch (NumberFormatException ignored) {
            }
        });

        tfC.addActionListener(e -> {
            if (rbCX.isSelected()) {
                try {
                    double kx = Double.parseDouble(tfC.getText());
                    myMouseAdapterSingleton.shearShape(kx, 0);
                } catch (NumberFormatException nfe) {
                    tfC.setText("0.0");
                }
            } else if (rbCY.isSelected()) {
                try {
                    double ky = Double.parseDouble(tfC.getText());
                    myMouseAdapterSingleton.shearShape(0, ky);
                } catch (NumberFormatException nfe) {
                    tfC.setText("0.0");
                }
            }
        });

        btEscala.addActionListener(e -> {
            double x = 1, y = 1;

            try {
                x = Double.parseDouble(tfEX.getText());
                tfEX.setText(String.valueOf(x));
            } catch (NumberFormatException nfe) {
                tfEX.setText("");
            }
            try {
                y = Double.parseDouble(tfEY.getText());
                tfEY.setText(String.valueOf(y));
            } catch (NumberFormatException nfe) {
                tfEY.setText("");
            }

            myMouseAdapterSingleton.scaleShape(x, y);
        });

        btFechoConvexo.addActionListener(e -> myMouseAdapterSingleton.convexHullFacade());

        frame.add(settings, BorderLayout.LINE_END);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
