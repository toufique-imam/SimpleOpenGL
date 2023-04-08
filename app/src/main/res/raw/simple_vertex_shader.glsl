uniform mat4 u_Matrix;

attribute vec4 a_Position; //The attribute keyword is how we feed these attributes into our shader.
//attribute vec4 a_Color;

//varying vec4 v_Color; //A varying is a special type of variable that blends the values given to it and sends these values to the fragment shader.

void main() {
//    v_Color = a_Color;

    gl_Position = u_Matrix*a_Position;
//    gl_PointSize = 10.0;
}