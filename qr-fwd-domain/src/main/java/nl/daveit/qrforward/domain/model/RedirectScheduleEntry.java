package nl.daveit.qrforward.domain.model;

import java.net.URI;
import java.time.LocalTime;

public record RedirectScheduleEntry(LocalTime start, LocalTime end, URI redirectUri) {}
