#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 3) in vec2 texCoord;

out vec2 textureCoord;

uniform mat4 spriteMatrix;
uniform mat4 cameraMatrix;
uniform vec4 spriteOffset;

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
        spriteCoords = vec2(spriteOffset.x, spriteOffset.z);
    } else if (uvCoord.x == 0 && uvCoord.y == 1 ) {
        spriteCoords = vec2(spriteOffset.y, spriteOffset.z);
    } else if (uvCoord.x == 1 && uvCoord.y == 0 ) {
        spriteCoords = vec2(spriteOffset.x, spriteOffset.w);
    } else {
        spriteCoords = vec2(spriteOffset.y, spriteOffset.w);
    }

    return spriteCoords;
}

void main()
{
    vec4 worldPos = spriteMatrix * vec4(aPos, 1.0);
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
