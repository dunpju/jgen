package io.dunpju.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 公共工具类
 *
 * @Author liuguoqiang
 * @Date 2023/5/23
 * @Version 1.0
 */
public class CommonUtils {

    /**
     * 列表按照subNum拆分
     *
     * @param list   列表
     * @param subNum 拆分级数
     * @param <T>    列表元素类
     * @return 拆分后的列表
     */
    public static <T> List<List<T>> splitList(List<T> list, int subNum) {
        return IntStream.iterate(0, i -> i + subNum)
                .limit((list.size() + subNum - 1) / subNum)
                .mapToObj(i -> list.subList(i, Math.min(i + subNum, list.size())))
                .toList();
    }

    /**
     * map按照subNum拆分
     *
     * @param map    map
     * @param subNum 拆分级数
     * @param <K>    key
     * @param <V>    value
     * @return 拆分后的map
     */
    public static <K, V> List<Map<K, V>> splitMap(Map<K, V> map, int subNum) {
        List<Map<K, V>> resultList = new ArrayList<>();

        if (map != null && !map.isEmpty()) {
            Map<K, V> currentBatchMap = new HashMap<>();
            int count = 0;

            for (Map.Entry<K, V> entry : map.entrySet()) {
                currentBatchMap.put(entry.getKey(), entry.getValue());
                count++;

                if (count >= subNum) {
                    resultList.add(currentBatchMap);
                    currentBatchMap = new HashMap<>();
                    count = 0;
                }
            }

            // 添加最后一个不满足 batchSize 的分批
            if (!currentBatchMap.isEmpty()) {
                resultList.add(currentBatchMap);
            }
        }

        return resultList;
    }

    /**
     * 获取列表中指定索引的元素，如果索引超出范围则返回默认值。
     *
     * @param list         要操作的列表
     * @param index        要获取元素的索引
     * @param defaultValue 索引超出范围时返回的默认值
     * @return 列表中对应索引的元素，或默认值
     */
    public static <T> T getListElementOrDefault(List<T> list, int index, T defaultValue) {
        if (CollectionUtils.isEmpty(list)) {
            return defaultValue;
        }
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            return defaultValue;
        }
    }

    public static void listSetOrAddToIndex(List<String> subParseList, int index, String subParse) {
        if (index < 0) {
            // 索引为负数时，约定在列表末尾添加元素
            subParseList.add(subParse);
        } else if (index < subParseList.size()) {
            // 索引在列表范围内，替换该位置的元素
            subParseList.set(index, subParse);
        } else {
            // 索引超出范围但在正数时，在列表末尾添加元素
            subParseList.add(subParse);
        }
    }

    /**
     * 按照html <p>标签拆分
     *
     * @param html html
     * @return 列表
     */
    public static List<String> splitHtmlIntoParagraphs(String html) {
        // 使用<p 开头作为拆分依据，但需要确保首个<p 不被移除
        if (!html.startsWith("<p")) {
            html = "<p>" + html;
        }
        String[] parts = html.split("(?<=</p>)");
        List<String> validParagraphs = new ArrayList<>();

        for (String part : parts) {
            if (part.trim().startsWith("<p")) {
                validParagraphs.add(part);
            }
        }

        return validParagraphs;
    }

    /**
     * 从Map中通过key获取对象，并使用提供的Function来从对象中提取值。
     * 如果map中没有该key，或者Function应用后结果为null，则返回defaultValue。
     *
     * @param map            存储对象的Map
     * @param key            查找对象的键
     * @param valueExtractor 从对象中提取值的Function
     * @param defaultValue   默认返回值
     * @param <K>            Map的键类型
     * @param <V>            Map的值类型
     * @param <R>            提取值的类型
     * @return Function应用的结果或默认值
     */
    public static <K, V, R> R getValueFromMap(Map<K, V> map, K key, Function<V, R> valueExtractor, R defaultValue) {
        V object = map.get(key);
        if (object == null) {
            return defaultValue;
        }

        R value = valueExtractor.apply(object);
        return value != null ? value : defaultValue;
    }

    /**
     * 根据keyList获取map中的元素
     * @param map map
     * @param keyList keyList
     * @return list
     * @param <K> key
     * @param <V> v
     */
    public static <K, V> List<V> getMapElementByKeyList(Map<K, V> map, List<K> keyList) {
        if (CollectionUtils.isEmpty(map) || CollectionUtils.isEmpty(keyList)) {
            return List.of();
        }
        return keyList.stream()
                .map(map::get)
                .filter(Objects::nonNull).
                collect(Collectors.toList());
    }

    /**
     * 根据逗号分割ids 返回int list
     * @param ids str
     * @return list
     */
    public static List<Integer> splitIntIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            return List.of();
        }
        return Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * 根据逗号分割ids 返回long list
     * @param ids str
     * @return list
     */
    public static List<Long> splitLongIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            return List.of();
        }
        return Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    /**
     * 将逗号分隔的ids字符串转换为Long Set
     * @param ids str
     * @return set
     */
    public static Set<Long> splitLongIdsToSet(String ids) {
        if (StringUtils.isBlank(ids)) {
            return Set.of();
        }
        return Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    /**
     * 将Long List 转换为逗号分隔的ids字符串
     * @param list list
     * @return str
     */
    public static String longListToString(List<Long> list) {
        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
