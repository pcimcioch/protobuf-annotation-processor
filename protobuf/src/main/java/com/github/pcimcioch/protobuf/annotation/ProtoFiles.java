package com.github.pcimcioch.protobuf.annotation;

import java.util.List;

/**
 * Set of all protobuf files
 *
 * @param files files
 */
public record ProtoFiles(List<ProtoFile> files) {
}
