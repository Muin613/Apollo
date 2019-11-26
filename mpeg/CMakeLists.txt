
cmake_minimum_required(VERSION 3.4.1)

#设置变量SRC_DIR为lamemp3的所在路径
set(SRC_DIR ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lamemp3)
set(ROOT_DIR ${CMAKE_CURRENT_SOURCE_DIR})
#指定头文件所在，可以多次调用，指定多个路径
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lamemp3)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/x264)
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include)
#添加自自定义的so库时，有两种方式，一种添加一个目录，一种一个个文件添加
message(STATUS "******************************************************************" ..)
add_library(libx264 SHARED IMPORTED)
set_target_properties(libx264
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libx264.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libx264.so)

add_library(libavcodec SHARED IMPORTED)
set_target_properties(libavcodec
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libavcodec.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libavcodec.so)

add_library(libavdevice SHARED IMPORTED)
set_target_properties(libavdevice
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libavdevice.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libavdevice.so)

add_library(libavfilter SHARED IMPORTED)
set_target_properties(libavfilter
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libavfilter.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libavfilter.so)

add_library(libavformat SHARED IMPORTED)
set_target_properties(libavformat
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libavformat.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libavformat.so)

add_library(libavutil SHARED IMPORTED)
set_target_properties(libavutil
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libavutil.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libavutil.so)

add_library(libpostproc SHARED IMPORTED)
set_target_properties(libpostproc
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libpostproc.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libpostproc.so)

add_library(libswresample SHARED IMPORTED)
set_target_properties(libswresample
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libswresample.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libswresample.so)

add_library(libswscale SHARED IMPORTED)
set_target_properties(libswscale
        PROPERTIES
        IMPORT_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/libs/libswscale.so)
link_libraries(${CMAKE_CURRENT_SOURCE_DIR}/libs/libswscale.so)

#设定一个目录
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/lamemp3 SRC_LIST)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/x264 SRC_LIST1)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libavcodec FF_AVCODEC)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libavdevice FF_AVDEVICE)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libavfilter FF_AVFILTER)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libavformat FF_AVFORMAT)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libavutil FF_AVUTIL)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libpostproc FF_POSTPROC)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libswresample FF_SWRESAMPLE)
aux_source_directory(${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/include/libswscale FF_SWSCALE)




#将前面目录下所有的文件都添加进去
add_library(lamemp3-lib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/LameMp3.cpp ${SRC_LIST})
add_library(native-lib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/apollo-video.cpp)
add_library(x264lib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/EncodeX264.cpp ${SRC_LIST1})
add_library(video-lib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/src/main/cpp/ffmpeg/FFVideoPlayer.cpp ${FF_AVCODEC} ${FF_AVDEVICE} ${FF_AVFILTER} ${FF_AVFORMAT} ${FF_AVUTIL} ${FF_POSTPROC} ${FF_SWRESAMPLE} ${FF_SWSCALE} ${SRC_LIST1})
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

find_library(log-lib log)

target_link_libraries(x264lib
        log
        ${libx264}
        )
target_link_libraries(video-lib
        ${libavcodec}
        ${libavdevice}
        ${libavfilter}
        ${libavformat}
        ${libavutil}
        ${libpostproc}
        ${libswresample}
        ${libswscale}
        ${libx264}
        android
        ${log-lib})