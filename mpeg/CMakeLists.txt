
cmake_minimum_required(VERSION 3.4.1)

#设置变量SRC_DIR为lamemp3的所在路径
set(SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lamemp3)
set(ROOT_DIR ${CMAKE_CURRENT_SOURCE_DIR})
#指定头文件所在，可以多次调用，指定多个路径
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lamemp3)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/x264)
#添加自自定义的so库时，有两种方式，一种添加一个目录，一种一个个文件添加
message(STATUS "******************************************************************" ..)
add_library(libx264 SHARED IMPORTED)
set_target_properties(libx264
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libx264.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libx264.so)
#设定一个目录
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lamemp3 SRC_LIST)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/x264 SRC_LIST1)

#将前面目录下所有的文件都添加进去
add_library(lamemp3-lib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/LameMp3.cpp ${SRC_LIST})
add_library(native-lib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/apollo-video.cpp)
add_library(x264lib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/EncodeX264.cpp ${SRC_LIST1})
#一个个文件的加
#add_library(lame-mp3
#            SHARED
#            ${SRC_DIR}/bitstream.c
#            ${SRC_DIR}/encoder.c
#            ${SRC_DIR}/fft.c
#            ${SRC_DIR}/gain_analysis.c
#            ${SRC_DIR}/id3tag.c
#            ${SRC_DIR}/lame.c
#            ${SRC_DIR}/mpglib_interface.c
#            ${SRC_DIR}/newmdct.c
#            ${SRC_DIR}/presets.c
#            ${SRC_DIR}/psymodel.c
#            ${SRC_DIR}/quantize.c
#            ${SRC_DIR}/quantize_pvt.c
#            ${SRC_DIR}/reservoir.c
#            ${SRC_DIR}/set_get.c
#            ${SRC_DIR}/tables.c
#            ${SRC_DIR}/takehiro.c
#            ${SRC_DIR}/util.c
#            ${SRC_DIR}/vbrquantize.c
#            ${SRC_DIR}/VbrTag.c
#            ${SRC_DIR}/version.c
#            )

find_library(log-lib log )

target_link_libraries(x264lib
        log
        ${libx264}
        )