precision mediump float;
uniform vec4 u_Color; // a uniform keeps the same value for all vertices until we change it again

void main() {
    gl_FragColor = u_Color;
}