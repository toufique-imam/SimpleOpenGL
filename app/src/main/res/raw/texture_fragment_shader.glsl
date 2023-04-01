precision mediump float;

uniform sampler2d u_TextureUnit;
varying vec2 v_TextureCoordinates;

void main() {
    gl_fragcolor = texture2D(u_TextureUnit, v_TextureCoordinates);
}