#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 3) in vec2 texCoord;

out vec2 textureCoord;

uniform mat4 textPosition;
uniform mat4 cameraMatrix;
uniform vec4 glyphOffset;
uniform vec2 glyphSize;

vec2 getSpriteLocation(vec2 uvCoord)
{
    // UVs
    // 0,0 ---- 0,1
    //  |        |
    //  |        |
    // 1,0 ---- 1,1

    // Coordinates from sprite sheet
    // x1,y1 ---- x2,y1
    //  |           |
    //  |           |
    // x1,y2 ---- x2,y2

    // vec4 mapping
    // x   y   z   w
    // x1  x2  y1  y2

    // result
    // x,z ---- y,z
    //  |        |
    //  |        |
    // x,w ---- y,w

    vec2 spriteCoords;
    if (uvCoord.x == 0 && uvCoord.y == 0) {
        spriteCoords = vec2(glyphOffset.x, glyphOffset.z);
    } else if (uvCoord.x == 0 && uvCoord.y == 1 ) {
        spriteCoords = vec2(glyphOffset.y, glyphOffset.z);
    } else if (uvCoord.x == 1 && uvCoord.y == 0 ) {
        spriteCoords = vec2(glyphOffset.x, glyphOffset.w);
    } else {
        spriteCoords = vec2(glyphOffset.y, glyphOffset.w);
    }

    return spriteCoords;
}

vec2 resiseSprite(vec3 pos, vec2 size)
{
//    float xPixels = 1 / (3824 / 2);
//    float yPixels = 1 / (2054 / 2);
    float xPixels = 1 / 1;
    return vec2 (pos.x * 0.01 * size.x, pos.y * 0.01 * size.y);
//    return vec2 (pos.x * xPixels * size.x, pos.y * yPixels * size.y);
}

vec2 resiseSpritehack(vec2 uvCoord, vec2 size)
{
    vec2 spriteCoords;
    if (uvCoord.x == 0 && uvCoord.y == 0) {
//        spriteCoords = vec2(size.y, 0);
        spriteCoords = vec2(0, size.y);
    } else if (uvCoord.x == 0 && uvCoord.y == 1 ) {
        spriteCoords = size;
//        spriteCoords = vec2(1,1);
    } else if (uvCoord.x == 1 && uvCoord.y == 0 ) {
        spriteCoords = vec2(0, 0);
    } else {
//        spriteCoords = vec2(0, size.y);
        spriteCoords = vec2(size.x,0);
    }

    return spriteCoords * 1 / (1000/2);
//    return spriteCoords * 0.001;
}

void main()
{
    vec2 newPos = resiseSpritehack(texCoord, glyphSize);
    vec4 worldPos = textPosition * vec4(newPos, aPos.z, 1.0);
    gl_Position = cameraMatrix * worldPos;
    textureCoord = getSpriteLocation(texCoord);
}

    #---
    #version 330 core

in vec2 textureCoord;

out vec4 FragColor;

uniform sampler2D textureSampler;

void main()
{
    FragColor = texture(textureSampler, textureCoord);
}
