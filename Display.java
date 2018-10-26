package com.example.joeym.playground;

/*
- make output distribution thing for ANY neuron, if it's a SELECTED neuron.
 */
public class Display {

    Network n;
    int neuronRadius = 20;
    int fontSize = 12;
    double thicknessMultiplier = 50;
    int buttonrows = 2;
    int buttonHeight = 40;

    public Display(Network n) {
        this.n = n;

        //buttons = new ArrayList();
    }
        /*addButton ("new", (ActionEvent e) -> {
            Thread t = new Thread (()->{
                Network.main (new String []{});
            });
            t.start ();
        });
        addButton ("close", (ActionEvent e) ->{
            setVisible(false);
            stop = true;
            dispose ();
        });
        addButton ("new training batch", (ActionEvent e)-> n.setQAArray());
        addButton ("train one batch",(ActionEvent e) -> n.runBatch());
        addButton (">/II slow training", (ActionEvent e) -> {
            if (stop) {
                stop = false;
                Thread tempThread = new Thread(() ->{
                    long time = System.currentTimeMillis();
                    while (!stop) {
                        //n.runBatch();
                        //try {
                        //    Thread.sleep(30);
                        //}
                        //catch (Exception f){
                        //    System.out.println ("sleeping failed");
                        //}

                        try {
                            Thread.sleep((int)(Math.max (-(System.currentTimeMillis () - time - 30), 0)));
                        } catch (Exception g) {
                            System.out.println("um");
                        }
                        time = System.currentTimeMillis();
                        n.runBatch();
                    }
                });
                tempThread.start();

            } else
                stop = true;
        });
        addButton (">/II fast training",(ActionEvent e) ->{
            if (stop){
                stop = false;
                Thread tempThread = new Thread (() ->{
                    while (!stop){
                        n.runBatch();
                    }
                });
                tempThread.start();
            }
            else
                stop = true;
        });
        addButton ("lr * 2", (ActionEvent e) -> n.lr *= 2.0);
        addButton ("lr / 2", (ActionEvent e) -> n.lr /= 2.0);
        addButton ("update network", (ActionEvent e) -> updateNetwork = true);

        setButtonPositions();
        for (int x = 0; x < buttons.size(); x ++)
            add (buttons.get(x));

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                updateNetwork = true;
            }
        });

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                Data.putData ("lastLocation", getX() + ", " + getY());
            }
        });

        String [] position = Data.getData ("lastLocation", "0, 0").split (", ");
        setLocation(Integer.parseInt (position [0]), Integer.parseInt (position [1]));

        addKeyListener(this);
        addMouseListener (this);

        setLayout (null);
        setVisible (true);

        requestFocus();

        halfred = new Color (255, 0, 0, 122);
        halfblue = new Color (0, 0, 255, 122);
        halfwhite = new Color (255, 255, 255, 122);

        Thread thread = new Thread (){
            long time = System.currentTimeMillis();
            @Override
            public void run (){
                while (true) {
                    process ();
                    repaint();
                    try {
                        Thread.sleep((int)(Math.max (-(System.currentTimeMillis () - time - 16), 0)));
                    } catch (Exception e) {
                        System.out.println("um");
                    }
                    //System.out.println(System.currentTimeMillis() - time);
                    time = System.currentTimeMillis();
                }
            }
        };
        thread.start ();
        startDrawing = true;
    }
    private boolean startDrawing = false;



    Color halfred;
    Color halfblue;
    Color halfwhite;

    boolean stop = true;

    ArrayList <JButton> buttons;

    private void addButton (String name, ActionListener e){
        JButton newButton = new JButton (name);
        newButton.addActionListener (e);
        buttons.add (newButton);
        newButton.setText("[" + ((char)(buttons.size()-1 + 97)) + "] " + buttons.get(buttons.size()-1).getText());
        newButton.addActionListener ((ActionEvent f)-> Display.this.requestFocus());//put the focus back onto the jframe
    }

    //should work for even and odd numbers of buttons
    private void setButtonPositions (){//needs to prioritize putting buttons near beginning for non-whole number fitting into rows
        int buttonWidth = windowWidth / (buttons.size() + (buttons.size()%2 == 1?1:0)) * buttonrows;
        for (int y = 0; y < buttonrows; y ++)
        for (int x = 0; x < buttons.size() / buttonrows + (buttons.size()%buttonrows != 0?1:0); x ++) {
            buttons.get(x + y * (buttons.size() / buttonrows)).setBounds(buttonWidth * x, windowHeight - buttonHeight * (y + 1),
                    buttonWidth - 1, buttonHeight);
        }
    }

    int windowWidth;
    int windowHeight;

    private void draw (Graphics g){
        try {
            g.setFont(g.getFont().deriveFont(fontSize * 1.0f));
            displayText = new ArrayList();
            windowWidth = getContentPane().getSize().width;
            windowHeight = getContentPane().getSize().height;

            int errorGraphWidth = windowWidth * 7 / 10;

            double max = 0;
            double min = Double.MAX_VALUE / 2.0;
            int left = windowWidth - errorGraphWidth;//n.errors.size()/2;
            try {
                for (int x = 1; x < n.errors.size(); x++) {//ignore first one lol
                    if (n.errors.get(x) > max)
                        max = n.errors.get(x);
                    if (n.errors.get(x) < min)
                        min = n.errors.get(x);
                }
                g.setColor(Color.BLACK);
                for (int x = 1; x < n.errors.size(); x++) {//ignore first one lol
                    int xPos = left + (int) (1.0 * x * errorGraphWidth / n.errors.size());
                    try {
                        int yPos = (int) (((max - min) - (n.errors.get(x) - min)) * 100 / (max - min));
                        g.drawRect(xPos, yPos, 1, 1);
                    } catch (Exception f) {
                    }
                }
            }
            catch (Exception e){}
            g.drawLine(left - 1, 0, left - 1, 101);
            g.drawLine(left - 1, 101, left + errorGraphWidth, 101);

            drawString(max + "", left - 1, -1, g, "topright");
            drawString ("error", left - 10, 50, g, "middleright");
            drawString(min + "", left - 1, 101, g, "bottomright");
            drawString((n.batchesRun > n.maxErrorRecording ? n.batchesRun - n.maxErrorRecording : 0)+"", left, 101, g, "topleft");
            drawString ("batch number", left + errorGraphWidth/2, 111, g, "topmiddle");
            drawString(n.batchesRun +"", left + errorGraphWidth, 101, g, "topright");

            //if (n.errors.size() > 0)
            //    displayText.add ("current errors: " + n.errors.get(Math.min (n.errors.size(), Network.maxErrorSize)-1)+"");

            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                for (int y = 0; y < nneurons; y++) {
                    XY thisNeuron = positions.get(x).get(y);

                    for (int z = 0; z < layers.get(x).get(y).inputs.size(); z++) {
                        XY neuronPos = getPosition(layers.get(x).get(y).inputs.get(z).n1);
                        XY inputNeuron = positions.get(neuronPos.x).get(neuronPos.y);

                        double val = layers.get(x).get(y).inputs.get(z).w;
                        if (val > 0) {
                            ((Graphics2D) g).setStroke(new BasicStroke((float) Math.sqrt(val * thicknessMultiplier)));
                            g.setColor(halfblue);
                        } else {
                            ((Graphics2D) g).setStroke(new BasicStroke((float) Math.sqrt(val * -thicknessMultiplier)));
                            g.setColor(halfred);
                        }
                        g.drawLine(inputNeuron.x + neuronRadius, inputNeuron.y, thisNeuron.x - neuronRadius, thisNeuron.y);

                        drawDouble(val, (thisNeuron.x + inputNeuron.x) / 2, (thisNeuron.y + inputNeuron.y) / 2, g, "middle");
                    }
                }
            }

            for (int x = 0; x < layers.size(); x++) {
                int nneurons = layers.get(x).size();
                for (int y = 0; y < nneurons; y++) {
                    XY thisNeuron = positions.get(x).get(y);
                    Neuron neuron = layers.get(x).get(y);

                    double bias = neuron.bias;
                    if (bias > 0) {
                        ((Graphics2D) g).setStroke(new BasicStroke((float) Math.sqrt(bias * thicknessMultiplier)));
                        g.setColor(Color.BLUE);
                    } else {
                        ((Graphics2D) g).setStroke(new BasicStroke((float) Math.sqrt(-bias * thicknessMultiplier)));
                        g.setColor(Color.RED);
                    }
                    g.drawOval(thisNeuron.x - neuronRadius,
                            thisNeuron.y - neuronRadius,
                            neuronRadius * 2, neuronRadius * 2);
                    drawDouble(neuron.value, thisNeuron.x, thisNeuron.y, g, "middle");
                    drawDouble(bias, thisNeuron.x, thisNeuron.y + neuronRadius + 10, g, "middle");
                }

            }

            for (int x = 0; x < n.inputs.size(); x++) {
                XY pos0 = getPosition(n.inputs.get(x));
                XY pos = positions.get(pos0.x).get (pos0.y);
                drawString("in " + x, pos.x - neuronRadius * 2, pos.y, g, "middle");
            }
            for (int x = 0; x < n.outputs.size(); x++) {
                XY pos0 = getPosition(n.outputs.get(x));
                XY pos = positions.get(pos0.x).get(pos0.y);
                drawString("out " + x, pos.x + neuronRadius * 2, pos.y, g, "middle");
            }



            //plot the data?

            int dataSquareSide = 100;

            left = windowWidth - dataSquareSide;
            int top = windowHeight/2 - dataSquareSide/2;

            double [] maxes = new double [n.thisBatchPoints.get(0).length];
            double [] mins = new double [n.thisBatchPoints.get(0).length];

            Arrays.fill (maxes, Double.MIN_VALUE/2.0);
            Arrays.fill (mins, Double.MAX_VALUE/2.0);

            for (int x = 0; x < n.thisBatchPoints.size(); x ++){
                for (int y = 0; y < n.thisBatchPoints.get(0).length; y ++){
                    if (n.thisBatchPoints.get (x) [y] > maxes [y])
                        maxes [y] = n.thisBatchPoints.get(x)[y];
                    if (n.thisBatchPoints.get(x)[y] < mins [y])
                        mins [y] = n.thisBatchPoints.get(x)[y];
                }
            }
            //displayText.add (maxes[0]+" " + mins[0]);
            //displayText.add (maxes[1]+" " + mins[1]);
            //displayText.add (maxes[2]+" " + mins[2]);

            if (n.thisBatchPoints.get(0).length == 3 && n.inputs.size() == 2){

                drawDouble (maxes [0], windowWidth, top + dataSquareSide, g, "topright");
                drawDouble (mins[0], windowWidth - dataSquareSide, top + dataSquareSide, g, "topleft");

                drawDouble (maxes[1], windowWidth - dataSquareSide, top, g, "topright");
                drawDouble (mins [1], windowWidth - dataSquareSide, top + dataSquareSide, g, "bottomright");

                for (int x = 0; x < n.thisBatchPoints.size(); x ++){
                    double [] thisCase = n.thisBatchPoints.get(x);

                    int xPosition = (int)(left + dataSquareSide * ((thisCase [0] - mins [0])/(maxes [0] - mins [0])));
                    int yPosition = (int)(top + dataSquareSide - dataSquareSide * ((thisCase [1] - mins [1])/(maxes [1] - mins [1])));
                    int color = (int)(((thisCase [2] - mins [2])/(maxes [2] - mins [2])) * 255.0 * 2.0 - 255.0);

                    //if (x == 0){
                    //    displayText.add (xPosition + ", " + yPosition + " - " + color);
                    //}

                    if (color > 255)
                        color = 255;
                    if (color < -255)
                        color = -255;

                    if (color < 0) {
                        color = -color;
                        g.setColor(new Color(color, 0, 0));
                    } else
                    g.setColor(new Color(0, 0, color));

                    g.drawRect (xPosition, yPosition, 1, 1);

                }
            }

            displayText.add (keyboard);
            displayText.add ("lr: " + n.lr);

            g.setColor(Color.BLACK);
            for (int x = 0; x < displayText.size(); x++)
                g.drawString(displayText.get(x), 0, fontSize * (x + 1));
        }
        catch (Exception e){
            //e.printStackTrace();
        }
    }
    ArrayList <String> displayText = new ArrayList ();

    private void drawString (String text, int x, int y, Graphics g, String position){
        int width = g.getFontMetrics().stringWidth(text);

        ((Graphics2D) g).setStroke(new BasicStroke(1));
        g.setColor (halfwhite);
        if (position.equals ("middle"))
            g.fillRect (x - width/2, y - fontSize/2+1, width, fontSize);
        if (position.equals ("topright"))
            g.fillRect (x - width, y + 1, width, fontSize);
        if (position.equals ("topmiddle"))
            g.fillRect (x - width/2, y + 1, width, fontSize);
        if (position.equals ("topleft"))
            g.fillRect (x, y + 1, width, fontSize);
        if (position.equals ("bottomright"))
            g.fillRect (x - width, y - fontSize + 1, width, fontSize);
        if (position.equals ("middleright"))
            g.fillRect (x - width, y - fontSize/2, width, fontSize);

        g.setColor (Color.BLACK);
        if (position.equals ("middle"))
            g.drawString (text, x - width/2, y + fontSize/2);
        if (position.equals ("topright"))
            g.drawString (text, x - width, y + fontSize);
        if (position.equals ("topmiddle"))
            g.drawString (text, x - width/2, y + fontSize);
        if (position.equals ("topleft"))
            g.drawString (text, x, y + fontSize);
        if (position.equals ("bottomright"))
            g.drawString (text, x - width, y);
        if (position.equals ("middleright"))
            g.drawString (text, x - width, y + fontSize/2);
    }
    private void drawDouble (double text, int x, int y, Graphics g, String position){
        drawString ((Math.round(text * 1000.0)/1000.0)+"", x, y, g, position);
    }
    private void drawDouble (double text, int decimals, int x, int y, Graphics g, String position){
        drawString ((Math.round(text * Math.pow (10, decimals))/Math.pow (10, decimals))+"", x, y, g, position);
    }

    boolean updateNetwork = true;
    public void process (){
        if (updateNetwork)
            updateNetwork();
        //System.out.println (keyboard);
    }

    ArrayList <ArrayList <Neuron>> layers = new ArrayList<ArrayList<Neuron>> ();
    ArrayList <ArrayList <XY>> positions = new ArrayList ();

    private XY getPosition (Neuron n){
        for (int x = 0; x < layers.size(); x ++){
            for (int y = 0; y < layers.get(x).size(); y ++){
                if (layers.get(x).get(y) == n)
                    return new XY (x, y);
            }
        }
        System.out.println ("neuron not found");
        return null;
    }

    private boolean working = false;
    public void updateNetwork (){
        if (updateNetwork && !working){
            updateNetwork = false;
            working = true;
            ArrayList<Neuron> neurons = n.neurons;
            layers = new ArrayList<ArrayList<Neuron>> ();
            layers.add (new ArrayList <Neuron>());
            for (int x = 0; x < neurons.size(); x ++){
                boolean incrementLayer = false;
                for (int y = 0; y < neurons.get(x).inputs.size(); y ++){
                    for (int z = 0; z < layers.get (layers.size()-1).size(); z ++) {
                        if (neurons.get(x).inputs.get(y).n1 == layers.get(layers.size()-1).get (z))
                            layers.add (new ArrayList <Neuron>());
                    }
                }
                layers.get(layers.size()-1).add (neurons.get(x));
            }


            int width = layers.size();
            windowWidth = getContentPane().getSize().width;
            windowHeight = getContentPane().getSize().height;

            positions = new ArrayList ();
            for (int x = 0; x < layers.size(); x ++){
                int nneurons = layers.get(x).size();
                positions.add (new ArrayList());
                for (int y = 0; y < nneurons; y ++){
                    positions.get(x).add (new XY((int)(windowWidth*(x+0.5)/(width+1)), windowHeight*(y+2)/(nneurons+3)));

                }

            }

            setButtonPositions();


            working = false;
        }
    }

    String keyboard="";

    //this method will only fire for typeable keys (characters)
    public void keyTyped(KeyEvent e){
        //System.out.print ("@" + e.getKeyChar () + "@");
    }
    public void keyPressed(KeyEvent e){
        //System.out.println (e.getKeyChar());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                break;
            default:
                if (!keyboard.contains (e.getKeyChar ()+"") && !e.isActionKey ())
                    keyboard = keyboard + (e.getKeyChar ());
                break;
        }
    }
    //maybe for debugging
    public void keyReleased(KeyEvent e){
        int location = keyboard.indexOf(e.getKeyChar());
        if (location != -1){
            keyboard = keyboard.substring(0, location) + keyboard.substring(location + 1, keyboard.length());
        }
        //System.out.println (e.getKeyChar());
        if (e.getKeyChar() - 97 >= 0 && e.getKeyChar() - 97 < buttons.size())
            buttons.get (e.getKeyChar() - 97).doClick();
    }

    public void mouseExited (MouseEvent event){//when mouse exits window range (doesn't need focus)

    }

    public void mouseReleased (MouseEvent event){//called when the mouse button is released

    }
    public void mousePressed (MouseEvent event){//called when the mouse button is pressed, but doesn't continue firing
        //System.out.println (event.getX() + ", " + event.getY());


        //put in click+dragging neurons (with neuronRadius and positions
        //when clicked on, make it the "focused" neuron, showing stats and extra buttons
    }
    public void mouseEntered (MouseEvent event){//when mouse enters window range (doesn't need focus)

    }
    public void mouseClicked (MouseEvent event){//does NOT get called after a click and drag
    }*/
}