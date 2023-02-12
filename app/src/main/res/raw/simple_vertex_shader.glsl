attribute vec4 a_Position; //The attribute keyword is how we feed these attributes into our shader.

void main() {
    gl_Position = a_Position;
    gl_PointSize = 10.0;
}